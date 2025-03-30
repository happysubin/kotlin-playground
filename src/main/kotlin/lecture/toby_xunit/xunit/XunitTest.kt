package lecture.toby_xunit.xunit

fun main() {
    val testSuite = TestCaseTest.suite()
    val result = TestResult()
    testSuite.run(result)
    println(result.getSummary())

    val testSuite2 = TestSuite(TestCaseTest::class.java)
    testSuite2.add(TestCaseTest("testTemplateMethod"))
    testSuite2.add(testSuite)
    testSuite2.add(testSuite)
    val result2 = TestResult()
    testSuite2.run(result2)
    println(result2.getSummary())
}