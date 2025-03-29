package lecture.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    fun testRunning(){
        val wasRun = WasRun("testMethod")
        Assert.assertEquals(false, wasRun.wasRun)
        wasRun.run()
        Assert.assertEquals(true, wasRun.wasRun)
    }
}