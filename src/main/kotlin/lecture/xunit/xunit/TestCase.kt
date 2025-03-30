package lecture.xunit.xunit

import java.lang.reflect.InvocationTargetException

abstract class TestCase(
    val methodName: String
) {

    fun run() {
        setUp()
        try {
    //      val method = this.javaClass.getMethod(methodName)
            val method = this::class.java.getMethod(methodName)
            method.invoke(this)
        }
        catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }
        catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        }
        catch (e: NoSuchMethodException) {
            throw RuntimeException(e)
        }
        tearDown()
    }

    protected open fun setUp() {}
    protected open fun tearDown() {}
}