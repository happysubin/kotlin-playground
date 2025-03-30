package lecture.xunit.xunit

import jdk.incubator.vector.VectorOperators

class TestSuite {

    val tests = mutableListOf<WasRun>()

    fun add(wasRun: WasRun) {
        tests.add(wasRun)
    }

    fun run(testResult: TestResult) {
        tests.forEach {
            it.run(testResult)
        }
    }
}
