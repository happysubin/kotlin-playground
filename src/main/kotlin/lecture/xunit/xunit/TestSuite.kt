package lecture.xunit.xunit

class TestSuite {

    val tests = mutableListOf<TestCase>()

    fun add(wasRun: TestCase) {
        tests.add(wasRun)
    }

    fun run(testResult: TestResult) {
        tests.forEach {
            it.run(testResult)
        }
    }
}
