package book.tdd.part02

fun main() {
    val test = WasRun("testMethod")
    println(test.wasRun)
    test.testMethod()
    println(test.wasRun)
}