package lecture.xunit.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var wasRun: WasRun? = null

    override fun setUp() {
        wasRun = WasRun("testMethod")
    }

    fun testRunning(){
        Assert.assertEquals(false, wasRun!!.wasRun)
        wasRun!!.run()
        Assert.assertEquals("setUp testMethod", wasRun!!.log!!)
        Assert.assertEquals(true, wasRun!!.wasRun)
    }

    fun testSetup() {
        Assert.assertEquals(false, wasRun!!.wasSetUp)
        wasRun!!.run()
        Assert.assertEquals("setUp testMethod", wasRun!!.log!!)
        Assert.assertEquals(true, wasRun!!.wasSetUp)
    }
}