package book.tdd.part02

abstract class TestCase(val methodName: String) {
    fun run() {
        try {
            val method = this::class.java.getMethod(methodName)
            method.invoke(this)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }
    }
}