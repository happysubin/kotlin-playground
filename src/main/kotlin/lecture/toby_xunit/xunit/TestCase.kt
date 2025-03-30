package lecture.toby_xunit.xunit

abstract class TestCase(
    private val methodName: String
): Test {

    override fun run(testResult: TestResult) {
        testResult.testStarted()
        setUp()
        try {
    //      val method = this.javaClass.getMethod(methodName)
            val method = this::class.java.getMethod(methodName)
            method.invoke(this)
        }
        catch(e: Exception) {
            testResult.testFailed()
        }

        tearDown()
    }

    protected open fun setUp() {}
    protected open fun tearDown() {}
}