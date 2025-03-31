package book.tdd.part02.xunit

class TestCaseTest(methodName: String): TestCase(methodName) {

    var test: WasRun? = null

    override fun setUp() {
        this.test = WasRun("testMethod")
    }

    fun testTemplateMethod() {
        test?.run()
        Assert.assertEqual("setUp testMethod tearDown", test!!.log)
    }
}