package book.kotlin_in_action.part_2.chapter_08

import java.io.BufferedReader
import java.io.FileReader
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * 코틀린에서 람다를 함수 인자로 너기는 구문이 if나 for와 같은 이룸 문장과 비슷하다.
 * 코틀린이 보통 람다를 무명 클래스로 컴파일하지만 그렇다고 람다 식을 사용할 대마다 새로운 클래스가 만들어지지는 않는다.
 * 람다가 변수를 포획하면 람다가 생성되는 시점마다 새로운 무명 클래스 객체가 생긴다.
 *
 * 이런 경우 실행 시점에 무명 클래스 생성에 따른 부가 비용이 든다.
 * 따라서 람다를 사용하는 구현은 똑같은 작업을 수행하는 일반 함수를 사용한 구현보다 덜 효율적이다.
 *
 * 반복되는 코드를 별도의 라이브러리 함수로 빼내되 컴파일러가 자바의 일반 명령문만큼 효율적인 코드를 생성하게 만들 수는 없을끼?
 * 사실 코틀린 컴파일에서는 이게 가능하다.
 *
 * inline 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트 코드로 바꿔치기 해준다.
 */

/**
 * 어떤 하수를 inline으로 선언하면 그 함수의 본문이 인라인된다.
 * 다른 말로하면 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다는 뜻이다.
 */

inline fun<T> synchronized(lock: Lock, action:() -> T): T {
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
}

fun foo(l: Lock) {
    println("Before sync")
    synchronized(l) {
        println("Action")
    }
    println("After Sync")
}

/**
 * 위 코틀린 코드와 동일한 바이트 코드를 생성한다.
 * synchronized 함수의 본문뿐 아니라 synchronized에 전달된 람다의 본문도 함께 인라이닝된다.
 * 람다의 본문에 의해 만들어지는 바이트코드는 그 람다를 ㅎㅊㄹ하는 코드 정의의 일부분으로 간주되기 때문에 코틀린 컴파일러는 그 람다를 함수 인터페이스로 구현하는 무명 클래스로 감싸지 않는다.
 *
 */

fun __foo__(l: Lock) {
    println("Before sync")
    l.lock()
    try {
        println("Action")
    } finally {
        l.unlock()
    }
    println("After sync")
}

/**
 * 인라인 함수를 호출하면서 람다를 넘기는 대신에 함수 타입의 변수를 넘길수도 있다.
 * 이런 경우 인라인 함수를 호출하는 코드 위치에서는 변수에 저장된 람다의 코드를 알 수 없다.
 * 따라서 람다 본문은 인라이닝되지 않고 synchronized 함수의 본문만 인라이닝 된다.
 */

class LockOwner(val lock: Lock) {
    fun runUnderLock(body: () -> Unit) {
        synchronized(lock, body)
    }
}

/**
 * 파라미터를 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 사용한다면 람다를 표현하는 객체가 어딘가는 존재해야 하기 때문에 람다를 인라이닝할 수 없다.
 * 일반적으로 인라인 함수의 본문에서 람다 식을 호출하거나 람다식을 인자로 전달받아 바로 호출하는 경우에는 그 람다를 인라이닝할 수 있다.
 */

fun <T, R> Sequence<T>.map(transform: (T) -> R): Sequence<R> {
    return TransformingSequence(this, transform) //여기서 람다를 전달. 결국 함수 인터페이스를 구현하는 클래스 인스턴스로 만들어야 한다.
}
class TransformingSequence<T, R>(seq: Sequence<R>, transform: (T) -> R) : Sequence<T> {
    override fun iterator(): Iterator<T> {
        TODO("Not yet implemented")
    }
}


//인라이닝 금지도 가능
inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) {
    //
}

data class P(val name: String, val age: Int)

fun readFirstLineFromFile(path: String): String {
    return BufferedReader(FileReader(path)).use { it.readLine() } // use 함수는 닫을 수 있는(Closeable) 자원에 대한 확장 함수며, 람다를 인자로 받는다..
}



fun main() {
    val l = ReentrantLock()
    synchronized(l, {})
    val people = listOf(P("Alice", 26), P("Bob", 31))

    /**
     * filter는 인라인 함수
     * filter 함수의 바이트 코드는 그 함수에 전달된 람다 본문의 바이트코드와 함께 filter를 호출한 위치에 들어간다.
     */
    println(people.filter { it.age < 30 })

    val result = mutableListOf(P("Alice", 26), P("Bob", 31))
    for(person in people) {
        if (person.age < 30) result.add(person)
    }
    println(result)

    println(people.filter{ it. age > 30}.map( P::name ))
    /**
     * 시퀀스는 람다를 저장해야하므로 람다를 인라인하지 않는다.
     * 시퀀스 연산에서는 람다가 인라이닝되지 않기 때문에 크기가 작은 컬렉션은 오히려 일반 컬렉션 연산이 더 성능이 나을수도 있다.
     * 시퀀스를 통해 성능을 향상시킬 수 있는 경우는 컬렉션 크기가 큰 경우 뿐이다.
     */


    /**
     * inline 키워드를 사용해도 람다를 인자로 받는 함수만 성능이 좋아질 가능성이 높다.
     * JVM은 코드 실행을 분석해서 가장 이익이 되는 방향으로 호출을 인라이닝한다.
     * 이런 과정은 바이트코드를 실제 기계어 코드로 번역하는 과정(JIT)에서 일어난다.
     *
     * 일반 함수 호출의 경우 JVM은 이미 강력하게 인라이닝을 지원한다.
     * 이런 JVM의 최적화를 활용하는 부분에서 따로 함수 코드를 중복할 필요가 없다.
     * 반면 코틀린 인라인 함수는 바이트 코드에서 각 함수 호출 지점을 함수 본문으로 대치하기 때문에 코드 중복이 생긴다.
     * 게다가 함수를 직접 호출하면 스택 트레이스가 더 깔끔해진다.
     *
     * 람다를 인자로 받는 함수를 인라이닝하면 이익이 더 많다.
     * 인라이닝을 통해 없앨 수 있는 부가 비용이 상당하다. 함수 호출 비용도 줄이고 람다를 표현하는 클래스와 람다 인스턴스에 해당하는 객체를 만들 필요도 없다.
     *
     * JVM은 함수 호출과 람다를 인라이닝해 줄 정도로 똑똑하지는 못하다.
     * 또한 인라이닝을 사용하면 일반 람다에서는 사용할 수 없는 몇 가지 기능을 사용할 수 있다. ex non-local
     *
     * inline 변경자르 ㄹ붙이면 코드 크기에 주의를 기울여야 한다. 바이트 코드가 매우 커질 수도 있다./
     */

}