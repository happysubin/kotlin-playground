package book.tdd.part02.xunit

fun main() {
    println(TestCaseTest("testTemplateMethod").run().getSummary())
    println(TestCaseTest("testResult").run().getSummary())
    println(TestCaseTest("testFailedResult").run().getSummary())
}