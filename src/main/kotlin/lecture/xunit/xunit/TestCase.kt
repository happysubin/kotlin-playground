package lecture.xunit.xunit

import java.lang.reflect.InvocationTargetException

abstract class TestCase(
    val methodName: String
) {

    fun run(testResult: TestResult) {
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
//        catch (e: ReflectiveOperationException) { //상위 예외로 catch
//            throw RuntimeException(e)
//        }

        tearDown()
    }

    protected open fun setUp() {}
    protected open fun tearDown() {}
}