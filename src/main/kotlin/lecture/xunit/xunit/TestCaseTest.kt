package lecture.xunit.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var wasRun: WasRun? = null

    override fun setUp() {
        wasRun = WasRun("testMethod")
    }

    fun testTemplateMethod()  {
        wasRun!!.run()
        Assert.assertEquals("setUp testMethod", wasRun!!.log!!)
    }
}