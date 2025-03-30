package lecture.xunit.xunit


/**
 * 일종의 애플리케이션 코드
 */
class WasRun(methodName: String): TestCase(methodName) {

    var log: String? = null
    var wasRun: Boolean

    init {
        wasRun = false
    }

    fun testMethod() {
        this.wasRun = true
        this.log += " testMethod"
    }

    override fun setUp() {
        this.wasSetUp = true
        this.log = "setUp"
    }
}
