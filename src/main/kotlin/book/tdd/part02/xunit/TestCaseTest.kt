package book.tdd.part02.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var test: WasRun? = null

    override fun setUp() {
        this.test = WasRun("testMethod")
    }

    fun testRunning() {
        Assert.assertEqual(false, test?.wasRun!!)
        test?.run()
        Assert.assertEqual(true, test?.wasRun!!)
    }

    fun testSetUp() {
        Assert.assertEqual(false, test?.wasSetUp!!)
        test?.run()
        Assert.assertEqual(true, test?.wasSetUp!!)
    }
}