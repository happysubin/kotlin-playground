package lecture.xunit.xunit


class TestSuite : Test {

    private val tests = mutableListOf< Test>()

    constructor(testClass: Class<out TestCase>) {
        try {
            testClass
                .declaredMethods
                .filter { it.name.startsWith("test") }
                .forEach {
                    //테스트 케이스의 생성자는 String이 넘어가므로 String을 사용. 그리고 메서드 이름을 넘김
                    add(testClass.getConstructor(String::class.java).newInstance(it.name))
                }
        } catch (e: ReflectiveOperationException) { //상위 예외로 catch
            throw RuntimeException(e)
        }
    }

    constructor() {}

    fun add(wasRun: Test) {
        tests.add(wasRun)
    }

    override fun run(testResult: TestResult) {
        tests.forEach {
            it.run(testResult)
        }
    }
}
