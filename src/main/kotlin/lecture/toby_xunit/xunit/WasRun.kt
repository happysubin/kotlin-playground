package lecture.toby_xunit.xunit


/**
 * 일종의 애플리케이션 코드
 */
class WasRun(methodName: String): TestCase(methodName) {

    var log: String? = null

    fun testMethod() {
        this.log += " testMethod"
    }

    fun testBrokenMethod() {
        throw AssertionError()
    }

    override fun setUp() {
        this.log = "setUp"
    }

    override fun tearDown() {
        this.log += " tearDown"
    }
}
