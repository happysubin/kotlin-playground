package lecture.xunit.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var wasRun: WasRun? = null

    fun testTemplateMethod()  {
        wasRun = WasRun("testMethod")
        wasRun!!.run()
        Assert.assertEquals("setUp testMethod tearDown", wasRun!!.log!!)
    }

    fun testResult() {
        wasRun = WasRun("testMethod")
        val result: TestResult = wasRun!!.run()
        Assert.assertEquals("1 run, 0 failed", result.getSummary())
    }
}