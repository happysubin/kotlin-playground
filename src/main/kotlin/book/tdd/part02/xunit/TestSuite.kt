package book.tdd.part02.xunit

class TestSuite: Test {

    val tests = mutableListOf<Test>()
    fun add(test: Test) {
        tests.add(test)
    }

    override fun run(result: TestResult) {
        tests.forEach {
            it.run(result)
        }
    }

}
