package book.kotlin_in_action.part_2.chapter_7


/**
 * data 클래스의 주 생성자에 들어있는 프로퍼티에 대해서는 컴파일러가 자동으로 ComponentN 함수를 만들어준다.
 */


fun printEntries(map: Map<String, String>) {
    for((key, value) in map) {
        println("$key $value")
    }
}

fun main() {
    val p = Point(10, 20)
    val (x, y) = p
    println("x   = ${x}")
    println("y   = ${y}")

    //위와 아래가 동일
    val x1 = p.component1()
    val y1 = p.component2()

    println("x1   = ${x1}")
    println("y1   = ${y1}")

    printEntries(mapOf("1" to "2", "3" to "4"))
}