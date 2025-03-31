package book.tdd.part02.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {


    fun testTemplateMethod() {
        val test = WasRun("testMethod")
        test.run()
        Assert.assertEqual("setUp testMethod tearDown", test.log)
    }

    fun testResult() {
        val test = WasRun("testMethod")
        val result: TestResult = test.run()
        Assert.assertEqual("1 run, 0 failed", result.getSummary())
    }

    fun testFailedResult() {
        val test = WasRun("testBrokenMethod")
        val result: TestResult = test.run()
        Assert.assertEqual("1 run, 1 failed", result.getSummary())
    }

    fun testResultFormatting() {
        val result = TestResult()
        result.testStarted()
        result.testFailed()
        Assert.assertEqual("1 run, 1 failed", result.getSummary())
    }
}