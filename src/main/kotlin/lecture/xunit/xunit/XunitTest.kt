package lecture.xunit.xunit

fun main() {
    val result = TestResult()
    TestCaseTest("testTemplateMethod").run(result)
    TestCaseTest("testResult").run(result)
    TestCaseTest("testFailedResultFormatting").run(result)
    TestCaseTest("testFailedResult").run(result)
    println(result.getSummary())
}