package book.tdd.part02.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var test: WasRun? = null

    override fun setUp() {
        this.test = WasRun("testMethod")
    }

    fun testRunning() {
        test?.run()
        Assert.assertEqual("setUp ", test!!.log)
    }

    fun testSetUp() {
        test?.run()
        Assert.assertEqual("setUp testMethod", test!!.log)
    }
}