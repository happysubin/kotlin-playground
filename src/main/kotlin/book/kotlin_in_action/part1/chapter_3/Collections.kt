package book.kotlin_in_action.part1.chapter_3

import java.util.*
import kotlin.collections.ArrayList

//가변 인ㅋ자 함수: 인자의 개수가 달라질 수 있는 함수 정의

fun main() {
    val list: List<String> = listOf("first", "second", "third")
    println(list.last())
    val numbers: Set<Int> = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    println(numbers.maxOrNull())
    //println(list.withIndex())

    val array = arrayOf("1", "@", "#")
    val result = listOf("1", *array) //스프레드 연산자가 배열의 내용을 펼쳐준다.
    println(result)


    /**
     * 중위 호출이라는 방식으로 to 라는 일반 메서드를 호출
     */
    val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
    val map2 = mapOf(1.to("one"), 7.to("seven"))

    for ((k, v) in map.entries) {
        println("$k : $v")
    }


}