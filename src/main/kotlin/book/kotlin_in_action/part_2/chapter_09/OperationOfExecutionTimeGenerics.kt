package book.kotlin_in_action.part_2.chapter_09

import java.util.*


/**
 * JVM의 제네릭스는 보통 타입 소거를 사용해 구현된다.
 * 이는 실행 시점에 제네릭 클래스의 인스턴스에 타입 인자 정보가 들어있지 않다는 뜻이다.
 *
 * 함수를 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있다.
 * 코틀린에서는 이를 실체화라고 부른다.
 *
 * 자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워진다.
 * 이는 제네릭 클래스 인스턴스가 그 인스턴스를 생성할 때 쓰인 타입 인자에 대한 정보를 유지하지 않는다는 뜻이다.
 *
 *
 * 타입 소거로 인해 생기는 한계를 알아보자.
 * 타입 인자를 따로 저장하지 않기 때문에 실행 시점에 타입 인자를 검사할 수 없다.
 * 예를 들어 어떤 리스트가 문자열로 이뤄진 리스트인지 다른 객체로 이뤄진 리스트인지를 실행 시점에 검사할 수 없다.
 * 일반적으로 말하면 is 검사에서 타입 인자로 지정한 타입을 검사할 수는 없다.
 */

fun main() {
//    if ("str" is List<String>) { 컴파일 오류 발생. 검사 불가능
//
//    }

    /**
     * 코틀린에서는 타입 인자를 명시하지 않고 제네릭 타입을 사용할 수 없다.
     * 그렇다면 어떤 값이 집합이나 맵이 아니라 리스트라는 사실을 어떻게 확인할 수 있을까? 바로 스타 프로젝션을 사용하는 것이다.
     *
     * 타입 파라미터가 2개 이상이라면 모든 타입 파라미터에 *를 포함시켜야 한다.
     * 인자를 알 수 없는 제네릭 타입을 표현할 때 스타프로젝션을 사용한다.
     *
     * as나 as? 캐스팅에도 여전히 제네릭 타입을 사용할 수 있다.
     * 하지만 기저 클래스는 같지만 타입인자가 다른 타입으로 캐스팅해도 여전히 캐스팅에 성공한다는 점을 조심해야한다.
     */

    printSum(listOf(1, 2, 3, 4, 5))
    //printSum(setOf(1, 2, 3, 4)) -> 우리가 작성한 예외
    //printSum(listOf("1", "2", "3", "4")) // as? 캐스팅은 성공하지만 다른 예외가 발생. 어떤 값이 List<Int>인지 검사가 불가능. sum이 실행되는 도중에 예외 발생 ClassCastException
    printSumV2(listOf(1, 2, 3, 4, 5))

    //코틀린 제네릭 타입의 타입 인자 정보는 실행 시점에 지워진다.
    //따라서 제네릭 클래스의 인스턴스가 있어도 그 인스턴스를 만들 때 사용한 타입 인자를 알아낼 수 없다.
    //제네릭 함수의 타입 인자도 마찬가지다. 제네릭 함수가 호출되도 그 함수의 본문에서는 호출 시 쓰인 타입 인자를 알 수 없다.
    //인라인 함수의 타입 파라미터는 실체화되므로 실행 시점에 인라인 함수의 타입 인자를 알 수 있다.

    println(isA<String>("abc"))
    println(isA<String>(123))


    val items = listOf("one", 2, "three")
    println(items.filterIsInstance<String>()) //문자열만 거르기
    //inline을 사용한 이유는 성능 향상이 아니라 실체화한 타입 파라미터를 사용하기 위함이다.

    val v1 = ServiceLoader.load(Int::class.java)// 이 구문은 java.lang.Class 참조를 얻는 방법이다.
    println("v1 = ${v1}")
    val v2 = loadService<Service>()
    println(v2)

}

fun printSum(c: Collection<*>) {
    val intList = c as? List<Int> //unchecked cast 라고 경고만 나오고 컴파일을 진행하므로 다음 코드처럼 값을 원하는 제네릭 타입으로 캐스팅해서 사용해도 된다.
        ?: throw IllegalArgumentException("List is expected")
    println(intList.sum())
}

fun printSumV2(c: Collection<Int>) {
    if(c is List<Int>) { //컴파일 시점에 검사
        println(c.sum())
    }
}

// 'reified' 키워드를 사용하면 런타임에도 제네릭 타입의 실제 타입 정보를 유지하고 접근할 수 있다
inline fun <reified T> isA(value:Any) = value is T

inline fun <reified T> loadService(): ServiceLoader<T>? {
    return ServiceLoader.load(T::class.java)
}

class Service()
/**
 * 다음과 같은 경우에 실체화한 타입 파라미터 사용 가능
 *
 * 1. 타입 검사와 캐스팅
 * 2. 코틀린 리플렉션 API
 * 3. 코틀린 타입에 대응하는 java.lang.Class를 얻기
 * 4. 다른 함수를 호출할 때 타입 인자로 사용
 *
 * 다음과 같은 일은 할 수 없다.
 *
 * 1. 타입 파라미터 클래스의 인스턴스 생성하기
 * 2. 타입 파라미터 클래스의 동반 객체 메서드 호출하기
 * 3. 실체화한 타입 파라미터를 요구하는 함수를 호출하면서 실체화하지 않은 타입 파라미터로 받은 타입을 타입 인자로 넘기기
 * 4. 클래스, 프로퍼티, 인라인 함수가 아닌 함수의 타입 파리미터를 reified로 지정하기
 */