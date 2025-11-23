# SingleQueryCountHolder 테스트 실패 - 완전 분석 보고서

## 목차
1. [문제 현상](#1-문제-현상)
2. [관련 라이브러리 구조](#2-관련-라이브러리-구조)
3. [소스 코드 분석](#3-소스-코드-분석)
4. [문제의 근본 원인](#4-문제의-근본-원인)
5. [왜 다른 방법들은 실패하는가](#5-왜-다른-방법들은-실패하는가)
6. [상용 환경 vs 테스트 환경](#6-상용-환경-vs-테스트-환경)
7. [해결 방법](#7-해결-방법)
8. [결론](#8-결론)

---

## 1. 문제 현상

### 1.1 증상

`DbLabApplicationTests`를 실행하면 테스트가 순차적으로 실패:

```
✅ 첫 번째 테스트 (`연관관계 없이 상품 저장`): 성공
   - Expected: INSERT 2개
   - Recorded: INSERT 2개

❌ 두 번째 테스트 (`연관관계를 조회한 상품 저장`): 실패
   - Expected: INSERT 2개
   - Recorded: INSERT 4개

❌ 세 번째 테스트 (`프록시를 사용한 상품 저장`): 실패
   - Expected: INSERT 2개
   - Recorded: INSERT 6개
```

**패턴**: 2, 4, 6 - 명확한 누적 현상

### 1.2 실행 로그

```
=== Test 1: 연관관계 없이 상품 저장 ===
Before clear - Grand Total: 0
After clear - Grand Total: 0
Hibernate: insert into vendor ...
Hibernate: insert into prod ...
✅ assertInsertCount(2) → 성공

=== Test 2: 연관관계를 조회한 상품 저장 ===
Before clear - Grand Total: 0
After clear - Grand Total: 0
Hibernate: insert into vendor ...
Hibernate: select ...
Hibernate: insert into prod ...
❌ assertInsertCount(2) → 실패 (Expected 2 but was 4)

=== Test 3: 프록시를 사용한 상품 저장 ===
Before clear - Grand Total: 0
After clear - Grand Total: 0
Hibernate: insert into vendor ...
Hibernate: insert into prod ...
❌ assertInsertCount(2) → 실패 (Expected 2 but was 6)
```

**이상한 점**: `Grand Total: 0`인데 왜 4, 6이 기록되는가?

### 1.3 회피 방법

```kotlin
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
```

이 어노테이션 주석을 풀면 모든 테스트가 성공하지만, **매우 느림**.

---

## 2. 관련 라이브러리 구조

### 2.1 의존성 관계

```
db-lab/build.gradle.kts
├── io.hypersistence:hypersistence-utils-hibernate-63:3.12.0
│   └── (제공하는 기능) SQLStatementCountValidator
│
└── com.github.gavlyukovskiy:datasource-proxy-spring-boot-starter:1.12.1
    └── net.ttddyy:datasource-proxy:1.11.0
        ├── QueryCountHolder (정적 ThreadLocal API)
        └── SingleQueryCountHolder (구현체)
```

### 2.2 application.yml 설정

```yaml
decorator:
  datasource:
    datasource-proxy:
      count-query: true  # ← SingleQueryCountHolder 빈 등록 활성화
```

### 2.3 스프링 빈 등록

`datasource-proxy-spring-boot-starter`의 자동 설정:

```java
@Bean
@ConditionalOnMissingBean
@ConditionalOnProperty(
    value = {"decorator.datasource.datasource-proxy.count-query"},
    havingValue = "true"
)
public QueryCountStrategy queryCountStrategy() {
    return new SingleQueryCountHolder();  // ← 스프링 싱글톤 빈으로 등록
}
```

**핵심**: `count-query: true` → `SingleQueryCountHolder`가 **스프링 빈**으로 등록됨

---

## 3. 소스 코드 분석

### 3.1 SingleQueryCountHolder 구조

실제 소스 코드:

```java
package net.ttddyy.dsproxy.listener;

public class SingleQueryCountHolder implements QueryCountStrategy {

    // 저장소 1: 인스턴스 변수 (모든 스레드가 공유)
    private ConcurrentMap<String, QueryCount> queryCountMap = new ConcurrentHashMap<>();

    private boolean populateQueryCountHolder = true;

    @Override
    public QueryCount getOrCreateQueryCount(String dataSourceName) {
        // 1. queryCountMap에서 QueryCount 가져오기 (없으면 생성)
        QueryCount queryCount = queryCountMap.get(dataSourceName);
        if (queryCount == null) {
            queryCountMap.putIfAbsent(dataSourceName, new QueryCount());
            queryCount = queryCountMap.get(dataSourceName);
        }

        // 2. ThreadLocal에도 같은 객체 등록
        if (this.populateQueryCountHolder) {
            QueryCountHolder.put(dataSourceName, queryCount);  // ← 핵심!
        }

        return queryCount;
    }

    // 저장소 1을 초기화하는 메서드
    public void clear() {
        this.queryCountMap.clear();
    }
}
```

**핵심 발견**:
- `queryCountMap`: 빈의 인스턴스 변수 (싱글톤이므로 애플리케이션 전체에서 공유)
- `QueryCountHolder.put()`: ThreadLocal에도 같은 `QueryCount` 객체를 등록

### 3.2 QueryCountHolder 구조

```java
package net.ttddyy.dsproxy;

public class QueryCountHolder {

    // 저장소 2: ThreadLocal (스레드별 독립)
    private static ThreadLocal<ConcurrentMap<String, QueryCount>> queryCountMapHolder =
        new ThreadLocal<ConcurrentMap<String, QueryCount>>() {
            @Override
            protected ConcurrentMap<String, QueryCount> initialValue() {
                return new ConcurrentHashMap<String, QueryCount>();
            }
        };

    public static QueryCount get(String dataSourceName) {
        final Map<String, QueryCount> map = queryCountMapHolder.get();
        return map.get(dataSourceName);
    }

    public static QueryCount getGrandTotal() {
        final QueryCount totalCount = new QueryCount();
        final Map<String, QueryCount> map = queryCountMapHolder.get();  // ThreadLocal에서 가져옴
        for (QueryCount queryCount : map.values()) {
            totalCount.setSelect(totalCount.getSelect() + queryCount.getSelect());
            totalCount.setInsert(totalCount.getInsert() + queryCount.getInsert());
            // ... 나머지 필드들도 합산
        }
        return totalCount;
    }

    public static void put(String dataSourceName, QueryCount count) {
        queryCountMapHolder.get().put(dataSourceName, count);
    }

    public static void clear() {
        queryCountMapHolder.get().clear();  // ThreadLocal의 Map만 비움
    }
}
```

**핵심 발견**:
- `queryCountMapHolder`: 정적 ThreadLocal 변수
- `clear()`: ThreadLocal의 Map만 비움 (내부 `QueryCount` 객체는 건드리지 않음)

### 3.3 QueryCount 구조

```java
public class QueryCount {
    private int select;
    private int insert;
    private int update;
    private int delete;
    // ... getters and setters

    // 실제 카운트를 증가시키는 메서드
    public void incrementSelect() { this.select++; }
    public void incrementInsert() { this.insert++; }
    // ...
}
```

**핵심**: `QueryCount`는 실제 카운트를 담는 **가변(mutable) 객체**

---

## 4. 문제의 근본 원인

### 4.1 두 개의 저장소

`SingleQueryCountHolder`는 **두 개의 독립적인 저장소**를 사용합니다:

```
┌─────────────────────────────────────────────────────────────┐
│ SingleQueryCountHolder (스프링 싱글톤 빈)                     │
│                                                              │
│ 저장소 1: ConcurrentMap<String, QueryCount> queryCountMap   │
│           ↓                                                  │
│           dataSource → QueryCount(insert=2) ←──┐            │
│                                                 │            │
└─────────────────────────────────────────────────┼───────────┘
                                                  │
                        (같은 객체 참조)          │
                                                  │
┌─────────────────────────────────────────────────┼───────────┐
│ QueryCountHolder (정적 ThreadLocal)             │            │
│                                                 │            │
│ 저장소 2: ThreadLocal<Map<String, QueryCount>>  │            │
│           ↓                                     │            │
│           dataSource → ─────────────────────────┘            │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**핵심**: 저장소 2는 저장소 1의 `QueryCount` **객체 참조를 복사**합니다!

### 4.2 쿼리 실행 시 동작 흐름

```
1. 쿼리 실행 (INSERT)
    ↓
2. QueryExecutionListener가 감지
    ↓
3. queryCountStrategy.getOrCreateQueryCount("dataSource") 호출
    ↓
4. queryCountMap.get("dataSource")
    - 없으면: 새 QueryCount() 생성
    - 있으면: 기존 QueryCount 반환 ← 문제의 시작!
    ↓
5. queryCount.incrementInsert()  ← 실제 카운트 증가
    ↓
6. QueryCountHolder.put("dataSource", queryCount)  ← ThreadLocal에 등록
```

### 4.3 단계별 시나리오

#### 첫 번째 테스트 실행

```
@BeforeEach setUp() 실행:
  QueryCountHolder.clear() 호출
  ↓
  저장소 1 (queryCountMap): 비어있음
  저장소 2 (ThreadLocal Map): 비어있음

쿼리 실행 (INSERT 2개):
  getOrCreateQueryCount("dataSource")
  ↓
  queryCountMap.get("dataSource") → null (없음)
  ↓
  새 QueryCount 생성: qc1 = new QueryCount()
  queryCountMap.put("dataSource", qc1)
  ↓
  qc1.incrementInsert()  // qc1.insert = 1
  qc1.incrementInsert()  // qc1.insert = 2
  ↓
  QueryCountHolder.put("dataSource", qc1)  // ThreadLocal에도 등록
  ↓
  저장소 1: dataSource → qc1(insert=2)
  저장소 2: dataSource → qc1(insert=2) ← 같은 객체!

assertInsertCount(2):
  SQLStatementCountValidator가 QueryCountHolder.getGrandTotal() 호출
  ↓
  ThreadLocal에서 qc1 가져옴 → insert = 2
  ✅ 성공!
```

#### @BeforeEach (두 번째 테스트 전)

```
@BeforeEach setUp() 실행:
  QueryCountHolder.clear() 호출
  ↓
  queryCountMapHolder.get().clear()
  ↓
  저장소 1 (queryCountMap): dataSource → qc1(insert=2) ← 그대로!
  저장소 2 (ThreadLocal Map): 비어짐! ← Map만 비움

  중요: qc1 객체 자체는 여전히 insert=2 상태!
```

#### 두 번째 테스트 실행

```
쿼리 실행 (INSERT 2개 + SELECT 1개):
  getOrCreateQueryCount("dataSource")
  ↓
  queryCountMap.get("dataSource") → qc1 발견! ← 이미 존재!
  ↓
  qc1.incrementInsert()  // qc1.insert = 3 (2 + 1)
  qc1.incrementInsert()  // qc1.insert = 4 (2 + 2)
  ↓
  QueryCountHolder.put("dataSource", qc1)  // ThreadLocal에 다시 등록
  ↓
  저장소 1: dataSource → qc1(insert=4)
  저장소 2: dataSource → qc1(insert=4) ← 같은 객체!

assertInsertCount(2):
  QueryCountHolder.getGrandTotal() 호출
  ↓
  ThreadLocal에서 qc1 가져옴 → insert = 4
  ❌ 실패! (Expected 2 but was 4)
```

#### 세 번째 테스트 실행

```
@BeforeEach: 동일한 문제 반복
  저장소 1: qc1(insert=4) 유지
  저장소 2: 비워짐

쿼리 실행 (INSERT 2개):
  qc1.insert = 4 + 2 = 6

assertInsertCount(2):
  ❌ 실패! (Expected 2 but was 6)
```

### 4.4 핵심 문제 정리

```
문제 1: SingleQueryCountHolder가 스프링 싱글톤 빈
  → queryCountMap이 애플리케이션 생명주기 동안 유지됨

문제 2: QueryCountHolder.clear()는 ThreadLocal의 Map만 비움
  → QueryCount 객체 자체는 초기화하지 않음

문제 3: getOrCreateQueryCount()가 기존 QueryCount를 재사용
  → 이미 누적된 값에 계속 더해짐

문제 4: 두 저장소가 같은 QueryCount 객체를 참조
  → 한쪽을 비워도 객체 자체는 변하지 않음
```

---

## 5. 왜 다른 방법들은 실패하는가

### 5.1 시도 1: `SQLStatementCountValidator.reset()`

```kotlin
@BeforeEach
fun setUp() {
    SQLStatementCountValidator.reset()
}
```

**내부 동작 추정**:
```java
public static void reset() {
    QueryCountHolder.clear();  // ThreadLocal만 비움
    // SingleQueryCountHolder 빈은 건드리지 않음!
}
```

**결과**: ❌ 실패 (2, 4, 6)

**이유**: `queryCountMap`은 그대로 유지되므로 `QueryCount` 객체가 초기화되지 않음

### 5.2 시도 2: `QueryCountHolder.clear()`

```kotlin
@BeforeEach
fun setUp() {
    QueryCountHolder.clear()
}
```

**내부 동작**:
```java
public static void clear() {
    queryCountMapHolder.get().clear();  // Map의 entry만 제거
    // QueryCount 객체는 초기화하지 않음
}
```

**시각화**:
```
clear() 전:
  ThreadLocal Map: { "dataSource" → qc1(insert=2) }
  queryCountMap:   { "dataSource" → qc1(insert=2) }

clear() 후:
  ThreadLocal Map: { } ← 비어짐
  queryCountMap:   { "dataSource" → qc1(insert=2) } ← 그대로!
  qc1 객체:        insert=2 ← 그대로!
```

**결과**: ❌ 실패 (2, 4, 6)

**이유**: `queryCountMap`의 `QueryCount` 객체가 초기화되지 않아 다음 테스트에서 재사용됨

### 5.3 시도 3: `count-query: false`

```yaml
decorator:
  datasource:
    datasource-proxy:
      count-query: false
```

**효과**:
```java
@ConditionalOnProperty(
    value = {"decorator.datasource.datasource-proxy.count-query"},
    havingValue = "true"  // ← false이므로 빈 등록 안 됨
)
public QueryCountStrategy queryCountStrategy() {
    return new SingleQueryCountHolder();  // ← 등록되지 않음
}
```

**결과**: ❌ 모든 테스트 실패 (recorded [0])

**이유**:
- `SingleQueryCountHolder` 빈이 등록되지 않음
- `hypersistence-utils`도 datasource-proxy에 의존하므로 카운트 불가
- `SQLStatementCountValidator`가 쿼리 개수를 추적할 수 없음

### 5.4 왜 `@DirtiesContext`는 성공하는가?

```kotlin
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
```

**동작**:
```
테스트 1 실행
  ↓
@DirtiesContext 작동
  ↓
스프링 컨텍스트 완전 종료
  ↓
SingleQueryCountHolder 빈 소멸 ← 여기가 핵심!
  queryCountMap 소멸
  모든 QueryCount 객체 소멸
  ↓
테스트 2 시작
  ↓
새로운 스프링 컨텍스트 생성
  ↓
새로운 SingleQueryCountHolder 빈 생성 ← 깨끗한 상태!
  새로운 queryCountMap (비어있음)
```

**결과**: ✅ 성공

**단점**:
- 각 테스트마다 컨텍스트 재시작 (매우 느림)
- 3개 테스트: 약 3초
- 100개 테스트: 약 100초 이상

---

## 6. 상용 환경 vs 테스트 환경

### 6.1 테스트 환경 (현재 문제)

```kotlin
@SpringBootTest
class DbLabApplicationTests {
    // SingleQueryCountHolder 빈은 자동으로 주입되지만
    // 직접 접근하지 않음

    @BeforeEach
    fun setUp() {
        // SQLStatementCountValidator가 제공하는 API만 사용
        SQLStatementCountValidator.reset()  // 불완전한 초기화
    }

    @Test
    fun test() {
        // 쿼리 실행
        SQLStatementCountValidator.assertInsertCount(2)  // 실패!
    }
}
```

**문제점**:
- `hypersistence-utils`의 API만 사용
- `SingleQueryCountHolder` 빈에 직접 접근하지 않음
- `SQLStatementCountValidator.reset()`이 빈을 완전히 초기화하지 못함

### 6.2 상용 환경 (올바른 사용법)

```kotlin
@Component
class QueryCountInterceptor : HandlerInterceptor {

    @Autowired
    private lateinit var queryCountStrategy: QueryCountStrategy  // ← 빈 주입!

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        // 요청 시작 시 완전한 초기화
        if (queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()  // queryCountMap 초기화
        }
        QueryCountHolder.clear()  // ThreadLocal 초기화

        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        // 쿼리 카운트 로깅
        val queryCount = QueryCountHolder.getGrandTotal()

        if (queryCount.total > 10) {
            log.warn("[Slow Query] path={}, queries={}", request.requestURI, queryCount.total)
        }

        log.info("[Query Count] path={}, SELECT={}, INSERT={}, UPDATE={}, DELETE={}",
            request.requestURI,
            queryCount.select,
            queryCount.insert,
            queryCount.update,
            queryCount.delete
        )

        // 요청 종료 후 다시 초기화
        if (queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }
        QueryCountHolder.clear()
    }
}
```

**핵심 차이점**:
- `SingleQueryCountHolder` 빈을 **직접 주입**받음
- `clear()` 메서드를 **명시적으로 호출**
- HTTP 요청 생명주기에 맞춰 **확실한 초기화/사용/초기화** 패턴

### 6.3 다른 상용 사례

#### 사례 1: AOP로 특정 메서드 모니터링

```kotlin
@Aspect
@Component
class QueryCountAspect {

    @Autowired
    private lateinit var queryCountStrategy: SingleQueryCountHolder

    @Around("@annotation(DetectNPlusOne)")
    fun detectNPlusOne(joinPoint: ProceedingJoinPoint): Any? {
        // 메서드 실행 전 초기화
        queryCountStrategy.clear()
        QueryCountHolder.clear()

        val result = joinPoint.proceed()

        // 메서드 실행 후 검증
        val queryCount = QueryCountHolder.getGrandTotal()
        if (queryCount.select > 5) {
            log.error("[N+1 Detected] method={}, SELECT queries={}",
                joinPoint.signature.toShortString(),
                queryCount.select
            )
            // 알림 전송, 메트릭 기록 등
        }

        queryCountStrategy.clear()
        QueryCountHolder.clear()

        return result
    }
}
```

#### 사례 2: 개발 환경에서 API 응답 헤더에 쿼리 수 추가

```kotlin
@Component
@Profile("dev")
class QueryCountResponseFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var queryCountStrategy: SingleQueryCountHolder

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        queryCountStrategy.clear()
        QueryCountHolder.clear()

        filterChain.doFilter(request, response)

        val queryCount = QueryCountHolder.getGrandTotal()
        response.setHeader("X-Query-Count-Total", queryCount.total.toString())
        response.setHeader("X-Query-Count-Select", queryCount.select.toString())
        response.setHeader("X-Query-Count-Insert", queryCount.insert.toString())

        queryCountStrategy.clear()
        QueryCountHolder.clear()
    }
}
```

### 6.4 비교 표

| 구분 | 테스트 환경 (문제) | 상용 환경 (정상) |
|------|-------------------|-----------------|
| **빈 주입** | ❌ 하지 않음 | ✅ 직접 주입받음 |
| **초기화 방법** | `SQLStatementCountValidator.reset()` | `queryCountStrategy.clear()` + `QueryCountHolder.clear()` |
| **초기화 범위** | ThreadLocal만 | queryCountMap + ThreadLocal 모두 |
| **초기화 시점** | `@BeforeEach` | HTTP 요청 시작/종료, 메서드 실행 전/후 |
| **생명주기 관리** | 스프링 테스트 컨텍스트 | HTTP 요청 또는 메서드 스코프 |
| **문제 발생** | ✅ 카운트 누적 | ❌ 없음 |

---

## 7. 해결 방법

### 7.1 해결책 1: SingleQueryCountHolder 빈 직접 주입 (★★★★★ 권장)

```kotlin
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest
class DbLabApplicationTests {

    @Autowired
    lateinit var prodRepository: ProdRepository

    @Autowired
    lateinit var prodForExternalSystemRepository: ProdForExternalSystemRepository

    @Autowired
    lateinit var vendorRepository: VendorRepository

    // SingleQueryCountHolder 빈 주입
    @Autowired(required = false)
    lateinit var queryCountStrategy: QueryCountStrategy

    @BeforeEach
    fun setUp() {
        // 저장소 1 초기화 (queryCountMap)
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }

        // 저장소 2 초기화 (ThreadLocal) + hypersistence-utils 초기화
        SQLStatementCountValidator.reset()
    }

    @AfterEach
    fun tearDown() {
        // 테스트 후 정리
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }
        SQLStatementCountValidator.reset()
    }

    @Test
    fun `연관관계를 조회한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.findById(vendor.id).orElseThrow())
        prodRepository.save(prod)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(1)
    }

    // 나머지 테스트들...
}
```

**장점**:
- ✅ 빠름 (컨텍스트 재시작 불필요)
- ✅ 정확한 초기화
- ✅ 상용 환경과 동일한 패턴
- ✅ 코드 변경 최소

**단점**:
- ❌ 내부 구현에 약간 의존 (`SingleQueryCountHolder` import 필요)

### 7.2 해결책 2: @DirtiesContext (★★★☆☆ 간단하지만 느림)

```kotlin
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DbLabApplicationTests {
    // 기존 코드 그대로 사용 가능

    @BeforeEach
    fun setUp() {
        SQLStatementCountValidator.reset()
    }

    // 테스트들...
}
```

**장점**:
- ✅ 100% 확실한 격리
- ✅ 코드 변경 최소
- ✅ 이해하기 쉬움

**단점**:
- ❌ 매우 느림 (각 테스트마다 컨텍스트 재시작)
- ❌ 테스트가 많아지면 비효율적

**권장 상황**:
- 테스트 개수가 적을 때 (< 10개)
- 빠른 프로토타이핑
- 일시적 회피책

### 7.3 해결책 3: Reflection으로 내부 상태 직접 초기화 (★★★★☆ 고급)

```kotlin
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder
import java.util.concurrent.ConcurrentMap

@SpringBootTest
class DbLabApplicationTests {

    @Autowired(required = false)
    lateinit var queryCountStrategy: QueryCountStrategy

    @BeforeEach
    fun setUp() {
        // Reflection으로 queryCountMap 직접 초기화
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            val field = SingleQueryCountHolder::class.java.getDeclaredField("queryCountMap")
            field.isAccessible = true
            val map = field.get(queryCountStrategy) as ConcurrentMap<*, *>
            map.clear()
        }

        SQLStatementCountValidator.reset()
    }

    // 테스트들...
}
```

**장점**:
- ✅ 빠름
- ✅ 확실한 초기화
- ✅ `clear()` 메서드가 없어도 동작

**단점**:
- ❌ Reflection 사용 (성능, 보안 이슈)
- ❌ 내부 필드명에 의존 (라이브러리 업데이트 시 깨질 수 있음)
- ❌ 코드 복잡도 증가

### 7.4 해결책 4: 테스트 전용 Configuration (★★★★☆ 깔끔)

```kotlin
@TestConfiguration
class TestQueryCountConfig {

    @Bean
    @Primary
    fun testQueryCountStrategy(): QueryCountStrategy {
        return TestQueryCountHolder()
    }

    class TestQueryCountHolder : QueryCountStrategy {
        private val queryCountMap = mutableMapOf<String, QueryCount>()

        override fun getOrCreateQueryCount(dataSourceName: String): QueryCount {
            return queryCountMap.getOrPut(dataSourceName) { QueryCount() }.also {
                QueryCountHolder.put(dataSourceName, it)
            }
        }

        fun clearAll() {
            queryCountMap.clear()
            QueryCountHolder.clear()
        }
    }
}

@SpringBootTest
@Import(TestQueryCountConfig::class)
class DbLabApplicationTests {

    @Autowired
    lateinit var queryCountStrategy: QueryCountStrategy

    @BeforeEach
    fun setUp() {
        (queryCountStrategy as TestQueryCountConfig.TestQueryCountHolder).clearAll()
        SQLStatementCountValidator.reset()
    }

    // 테스트들...
}
```

**장점**:
- ✅ 테스트 전용 구현으로 완벽한 제어
- ✅ 깔끔한 API
- ✅ 라이브러리 내부에 의존하지 않음

**단점**:
- ❌ 코드량 증가
- ❌ `QueryCountStrategy` 인터페이스 전체 구현 필요

### 7.5 해결책 5: 테스트 클래스 분리 (★★☆☆☆ 비추천)

```kotlin
// Test1.kt
@SpringBootTest
class ProductSaveWithAssociationTest {
    @Test
    fun `연관관계를 조회한 상품 저장`() {
        // 테스트...
    }
}

// Test2.kt
@SpringBootTest
class ProductSaveWithoutAssociationTest {
    @Test
    fun `연관관계 없이 상품 저장`() {
        // 테스트...
    }
}

// Test3.kt
@SpringBootTest
class ProductSaveWithProxyTest {
    @Test
    fun `프록시를 사용한 상품 저장`() {
        // 테스트...
    }
}
```

**동작 원리**:
- Gradle/JUnit은 기본적으로 테스트 **클래스**마다 독립적으로 실행
- 각 클래스가 별도 프로세스 또는 격리된 컨텍스트에서 실행될 수 있음

**장점**:
- ✅ 완벽한 격리
- ✅ 설정 불필요

**단점**:
- ❌ 파일 개수 폭증
- ❌ 중복 코드 (setUp, tearDown 등)
- ❌ 유지보수 어려움
- ❌ 전체 테스트 시간 증가

### 7.6 해결책 비교표

| 해결책 | 속도 | 난이도 | 안정성 | 유지보수 | 추천도 |
|--------|------|--------|--------|----------|--------|
| 1. 빈 직접 주입 | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ★★★★★ |
| 2. @DirtiesContext | ⭐ | ⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ★★★☆☆ |
| 3. Reflection | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ | ★★★★☆ |
| 4. TestConfiguration | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ★★★★☆ |
| 5. 클래스 분리 | ⭐⭐ | ⭐ | ⭐⭐⭐⭐ | ⭐ | ★★☆☆☆ |

### 7.7 최종 권장 사항

#### 개발 단계 (현재):
```kotlin
// 해결책 1 사용
@Autowired(required = false)
lateinit var queryCountStrategy: QueryCountStrategy

@BeforeEach
fun setUp() {
    if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
        (queryCountStrategy as SingleQueryCountHolder).clear()
    }
    SQLStatementCountValidator.reset()
}
```

#### 프로덕션 테스트 (CI/CD):
- 해결책 1 또는 해결책 4 사용
- 테스트 개수가 많으면 속도가 중요하므로 @DirtiesContext 피하기

---

## 8. 결론

### 8.1 문제의 본질

```
SingleQueryCountHolder는 두 개의 저장소를 사용하는 이중 구조:

1. queryCountMap (빈의 인스턴스 변수)
   - 실제 QueryCount 객체 저장
   - 스프링 싱글톤 빈이므로 계속 유지
   - clear() 메서드로 초기화 가능

2. ThreadLocal (정적 변수)
   - queryCountMap의 QueryCount를 참조
   - QueryCountHolder.clear()로 초기화
   - Map만 비워지고 QueryCount 객체는 그대로

문제:
- QueryCountHolder.clear()는 ThreadLocal만 비움
- queryCountMap의 QueryCount는 초기화되지 않음
- 다음 쿼리 실행 시 기존 QueryCount를 재사용하여 카운트 누적
```

### 8.2 핵심 발견

1. **SingleQueryCountHolder.clear() 메서드가 존재**하지만, 테스트 코드에서 사용하지 않았음
2. **hypersistence-utils의 SQLStatementCountValidator**는 이 메서드를 호출하지 않음
3. **상용 환경**에서는 빈을 직접 주입받아 clear() 호출하는 것이 정석
4. **테스트 환경**에서도 동일한 패턴을 따라야 함

### 8.3 교훈

#### 1. 라이브러리 사용 시 내부 구조 이해의 중요성
```
hypersistence-utils는 편리한 API를 제공하지만,
내부적으로 datasource-proxy에 의존하므로
두 라이브러리의 관계를 이해해야 올바르게 사용 가능
```

#### 2. 스프링 빈의 생명주기 관리
```
싱글톤 빈이 상태를 가지면 (Stateful Singleton),
명시적인 초기화가 필요하며,
이를 간과하면 테스트 격리가 깨짐
```

#### 3. ThreadLocal의 한계
```
ThreadLocal.clear()는 ThreadLocal 자체만 비우고,
참조하는 객체의 상태는 변경하지 않음
따라서 객체 자체의 초기화도 필요
```

#### 4. API 문서만으로는 부족
```
SQLStatementCountValidator.reset()이라는 이름만 보고
완전한 초기화를 기대했지만,
실제로는 불완전한 초기화였음
소스 코드 확인이 중요
```

### 8.4 이것은 테스트용 라이브러리인가?

**아니요. 상용 환경에서도 매우 유용합니다.**

하지만:
- **올바른 사용법을 따라야 함**
- **SingleQueryCountHolder 빈을 주입받아 직접 관리**
- **HTTP 요청, 메서드 실행 등 명확한 스코프에서 초기화/사용/초기화 패턴**

테스트에서 문제가 발생한 이유:
- hypersistence-utils의 편의 API만 사용
- 내부 구조를 이해하지 못함
- 상용 환경의 사용 패턴과 다른 방식으로 접근

### 8.5 최종 체크리스트

#### ✅ 해야 할 것:
- [ ] `SingleQueryCountHolder` 빈을 `@Autowired`로 주입
- [ ] `@BeforeEach`에서 `queryCountStrategy.clear()` 호출
- [ ] `SQLStatementCountValidator.reset()`도 함께 호출
- [ ] 상용 환경에서도 동일한 패턴 적용

#### ❌ 하지 말아야 할 것:
- [ ] `QueryCountHolder.clear()`만으로 초기화 시도
- [ ] `count-query: false`로 설정 (카운트 자체가 안 됨)
- [ ] 모든 테스트에 `@DirtiesContext` 남용 (느림)
- [ ] 내부 구조 이해 없이 API만 사용

### 8.6 참고 자료

- `net.ttddyy:datasource-proxy` 소스 코드:
  - `SingleQueryCountHolder.java`
  - `QueryCountHolder.java`
  - `QueryCount.java`

- `io.hypersistence:hypersistence-utils` 문서:
  - `SQLStatementCountValidator` API

- Spring Boot:
  - `@DirtiesContext` 문서
  - Bean Lifecycle 관리

---

## 부록: 전체 코드 예시

### A.1 수정된 테스트 코드 (해결책 1 적용)

```kotlin
package com.dblab

import com.dblab.jpa.proxy.*
import io.hypersistence.utils.jdbc.validator.SQLStatementCountValidator
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder.QueryCountStrategy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DbLabApplicationTests {

    @Autowired
    lateinit var prodRepository: ProdRepository

    @Autowired
    lateinit var prodForExternalSystemRepository: ProdForExternalSystemRepository

    @Autowired
    lateinit var vendorRepository: VendorRepository

    @Autowired(required = false)
    lateinit var queryCountStrategy: QueryCountStrategy

    @BeforeEach
    fun setUp() {
        // SingleQueryCountHolder 빈의 queryCountMap 초기화
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }

        // ThreadLocal 및 hypersistence-utils 초기화
        SQLStatementCountValidator.reset()
    }

    @AfterEach
    fun tearDown() {
        // 테스트 후 정리
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }
        SQLStatementCountValidator.reset()
    }

    @Test
    fun `연관관계를 조회한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.findById(vendor.id).orElseThrow())
        prodRepository.save(prod)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(1)
    }

    @Test
    fun `연관관계 없이 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = ProdForExternalSystem(name = "상품", vendorId = vendor.id)
        prodForExternalSystemRepository.save(prod)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(0)
    }

    @Test
    fun `프록시를 사용한 상품 저장`() {
        val vendor = vendorRepository.save(createVendor())
        val prod = Prod(name = "상품", vendor = vendorRepository.getReferenceById(vendor.id))
        prodRepository.save(prod)
        SQLStatementCountValidator.assertInsertCount(2)
        SQLStatementCountValidator.assertSelectCount(0)
    }

    private fun createVendor() = Vendor(
        name = "test_vendor",
        code = "code"
    )
}
```

### A.2 상용 환경 예시 코드

```kotlin
package com.dblab.config

import net.ttddyy.dsproxy.QueryCountHolder
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder.QueryCountStrategy
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class QueryCountInterceptor : HandlerInterceptor {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired(required = false)
    private lateinit var queryCountStrategy: QueryCountStrategy

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        // 요청 시작 시 완전한 초기화
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }
        QueryCountHolder.clear()

        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val queryCount = QueryCountHolder.getGrandTotal()

        // N+1 쿼리 경고
        if (queryCount.select > 10) {
            log.warn("[Slow Query] path={}, SELECT queries={}",
                request.requestURI, queryCount.select)
        }

        // 개발 환경에서 응답 헤더에 추가
        if (request.getHeader("X-Debug")?.equals("true") == true) {
            response.setHeader("X-Query-Count-Total", queryCount.total.toString())
            response.setHeader("X-Query-Count-Select", queryCount.select.toString())
            response.setHeader("X-Query-Count-Insert", queryCount.insert.toString())
        }

        log.debug("[Query Count] path={}, queries={}", request.requestURI, queryCount.total)

        // 요청 완료 후 정리
        if (::queryCountStrategy.isInitialized && queryCountStrategy is SingleQueryCountHolder) {
            (queryCountStrategy as SingleQueryCountHolder).clear()
        }
        QueryCountHolder.clear()
    }
}
```

---

**작성 일시**: 2025-11-22
**분석 대상**: `db-lab` 모듈 테스트 실패
**핵심 원인**: SingleQueryCountHolder 싱글톤 빈의 불완전한 초기화
**권장 해결책**: 빈 직접 주입 후 clear() 호출
