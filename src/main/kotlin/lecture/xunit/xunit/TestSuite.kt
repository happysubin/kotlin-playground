package lecture.xunit.xunit

class TestSuite: Test {

    val tests = mutableListOf< Test>()

    fun add(wasRun: Test) {
        tests.add(wasRun)
    }

    override fun run(testResult: TestResult) {
        tests.forEach {
            it.run(testResult)
        }
    }
}
