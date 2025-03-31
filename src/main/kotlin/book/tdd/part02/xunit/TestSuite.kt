package book.tdd.part02.xunit

class TestSuite {

    val tests = mutableListOf<TestCase>()
    fun add(test: TestCase) {
        tests.add(test)
    }

    fun run(result: TestResult) {
        tests.forEach {
            it.run(result)
        }
    }

}
