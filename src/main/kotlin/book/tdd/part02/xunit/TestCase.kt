package book.tdd.part02.xunit

abstract class TestCase(private val methodName: String) {

    var wasSetUp: Boolean? = null

    init {
        wasSetUp = false
    }

    fun run(): TestResult {
        setUp()
        val result = TestResult()
        result.testStarted()
        try {
            val method = this::class.java.getMethod(methodName)
            method.invoke(this)
        } catch(e: Exception) {
            result.testFailed()
        }
        tearDown()
        return result
    }

    protected open fun setUp() {}
    protected open fun tearDown() {}
}