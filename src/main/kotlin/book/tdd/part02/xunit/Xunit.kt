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


    val suite2 = TestSuite()

    suite2.add(suite)
    suite2.add(TestCaseTest("testResult"))
    suite2.add(TestCaseTest("testFailedResult"))
    val result2 = TestResult()
    suite2.run(result2)
    println(result2.getSummary())
}