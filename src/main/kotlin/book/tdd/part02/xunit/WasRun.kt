package book.tdd.part02.xunit



/**
 * 메서드가 실행되었는지 알려주는 테스트 케이스이므로, WasRun
 */
class WasRun(methodName: String): TestCase(methodName) {

    var wasRun: Boolean? = null

    init {
        wasRun = false
    }

    override fun setUp() {
        this.wasSetUp = true
    }

    fun testMethod() {
        wasRun = true
    }
}
