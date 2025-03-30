package book.tdd.part02

import java.lang.reflect.InvocationTargetException

/**
 * 메서드가 실행되었는지 알려주는 테스트 케이스이므로, WasRun
 */
class WasRun(val methodName: String) {

    var wasRun: Boolean

    init {
        wasRun = false
    }

    fun testMethod() {
        wasRun = true
    }

    fun run() {
        try {
            val method = this::class.java.getMethod(methodName)
            method.invoke(this)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }

    }
}
