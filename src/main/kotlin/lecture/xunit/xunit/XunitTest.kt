package lecture.xunit.xunit

fun main() {
    val testSuite = TestCaseTest.suite()
    val result = TestResult()
    testSuite.run(result)
    println(result.getSummary())
}