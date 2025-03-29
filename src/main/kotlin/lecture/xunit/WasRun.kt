package lecture.xunit


/**
 * 일종의 애플리케이션 코드
 */
class WasRun(methodName: String): TestCase(methodName) {
    var wasRun: Boolean

    init {
        wasRun = false
    }

    fun testMethod() {
        wasRun = true
    }
}
