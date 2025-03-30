package lecture.xunit.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var wasRun: WasRun? = null

    companion object {
        fun suite(): TestSuite {
            return TestSuite(TestCaseTest::class.java)
        }
    }

    fun testTemplateMethod()  {
        wasRun = WasRun("testMethod")
        val result = TestResult()
        wasRun!!.run(result)
        Assert.assertEquals("setUp testMethod tearDown", wasRun!!.log!!)
    }

    fun testResult() {
        wasRun = WasRun("testMethod")
        val result = TestResult()
        wasRun!!.run(result)
        Assert.assertEquals("1 run, 0 failed", result.getSummary())
    }

    fun testFailedResultFormatting() {
        val result = TestResult()
        result.testStarted()
        result.testFailed()
        Assert.assertEquals("1 run, 1 failed", result.getSummary())
    }

    fun testFailedResult() {
        wasRun = WasRun("testBrokenMethod")
        val result = TestResult()
        wasRun!!.run(result)
        Assert.assertEquals("1 run, 1 failed", result.getSummary())
    }

    fun testSuite() {
        val testSuite = TestSuite()
        testSuite.add(WasRun("testMethod"))
        testSuite.add(WasRun("testBrokenMethod"))
        val result = TestResult()
        testSuite.run(result)
        Assert.assertEquals("2 run, 1 failed", result.getSummary())
    }
}