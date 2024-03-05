package book.kotlin_in_action.part_2.chapter_7

import java.time.LocalDate

fun main() {
    val map = mutableMapOf("1" to Point(1, 3))
    val list = mutableListOf(Point(1, 2))

    //둘다 정상 출력
    println(map["1"])
    println(list[0])

    //아래는 동일
    println(list.contains(Point(1, 2)))
    println(Point(1, 2) in list)

    //start..end 와 start.rangeTo(end)

}