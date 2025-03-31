package book.tdd.part02.xunit

fun main() {
    val suite = TestSuite()

    suite.add(TestCaseTest("testTemplateMethod"))
    suite.add(TestCaseTest("testResult"))
    suite.add(TestCaseTest("testFailedResult"))
    suite.add(TestCaseTest("testResultFormatting"))
    suite.add(TestCaseTest("testSuite"))

    val result = TestResult()
    suite.run(result)
    println(result.getSummary())
}