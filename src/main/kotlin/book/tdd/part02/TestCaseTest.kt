package book.tdd.part02

class TestCaseTest(methodName: String): TestCase(methodName) {

    fun testRunning() {
        val test = WasRun("testMethod")
        println(test.wasRun)
        test.run()
        println(test.wasRun)
    }
}