package book.tdd.part02.xunit

abstract class TestCase(private val methodName: String) {

    var wasSetUp: Boolean? = null

    init {
        wasSetUp = false
    }

    fun run() {
        setUp()
        try {
            val method = this::class.java.getMethod(methodName)
            method.invoke(this)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }
    }

    protected open fun setUp() {}
}