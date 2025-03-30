package lecture.xunit.xunit

fun main() {
    val testSuite = TestSuite()
    val result = TestResult()
    testSuite.add(TestCaseTest("testTemplateMethod"))
    testSuite.add(TestCaseTest("testResult"))
    testSuite.add(TestCaseTest("testFailedResultFormatting"))
    testSuite.add(TestCaseTest("testFailedResult"))
    testSuite.add(TestCaseTest("testSuite"))
    testSuite.run(result)
    println(result.getSummary())
}