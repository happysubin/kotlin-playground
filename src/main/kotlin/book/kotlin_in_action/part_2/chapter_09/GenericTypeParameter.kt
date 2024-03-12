package book.kotlin_in_action.part_2.chapter_09

import java.lang.Appendable

fun main() {

    val letters = ('a'..'z').toList()
    println(letters.slice<Char>(0..2))
    println(letters.slice(10..13)) //컴파일러는 여기서 T가 Char라는 사실을 추론한다.

    val authors = listOf("Dmitry", "Svetlana")
    val readers = mutableListOf<String>("Dmitry", "bin", "bean")
    println(readers.filter { it !in authors }) //it의 타입은 T라는 제네릭 타입이다.

    println(listOf(1, 2, 3, 4).penultimate)

    println(listOf(1, 2, 3).sum())

    println(oneHalf(3))
    /**
     * 타입 파라미터 제약은 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다.
     * 어떤 타입을 제네릭 타입의 타입 파라미터에 대한 사한으로 지정하면 그 제네릭 타입을 인스턴스화할 때 사용하는 타입 인자는 반드시 그 상한 타입이거나 그 상한 타입의 하위 타입이어야 한다.
     *
     * 제약을 가하려면 타입 파라미터 일므 뒤에 콜론을 표시하고 그 뒤에 상한 타입을 적으면 된다.
     */

    println(max("kotlin", "java"))


    val helloWorld = StringBuilder("Hello World")
    ensureTrailingPeriod(helloWorld)
    println(helloWorld)

    val nullableStringProcessor = Processor<String?>()
    nullableStringProcessor.process(null)

    val nullableStringProcessor2 = ProcessorV2<String>()
    //nullableStringProcessor2.process(null) 컴파일 에러
}

val <T> List<T>.penultimate: T
    get() = this[size - 2]


fun <T: Number> oneHalf(value: T): Double {
    return value.toDouble() / 2.0
}

//String이 Comparable<String>을 확장한다.
fun <T: Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second //코틀린 연산자에 따라 first.compareTo(second) > 0 이라고 컴파일 된다.
}

fun <T> ensureTrailingPeriod(seq: T)
    where T: CharSequence, T : Appendable {
        if(!seq.endsWith('.')) {
            seq.append('.')
        }
    }

class Processor<T> {
    fun process(value: T) {
        value?.hashCode()
    }
}


class ProcessorV2<T: Any> { //null이 될 수 없는 타입 상한을 지정
    fun process(value: T) {
        value.hashCode()
    }
}