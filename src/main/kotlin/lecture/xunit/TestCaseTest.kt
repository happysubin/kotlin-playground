package lecture.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    fun testRunning(){
        val wasRun = WasRun("testMethod")
        println(wasRun.wasRun) //false
        wasRun.run()
        println(wasRun.wasRun) //true
    }
}