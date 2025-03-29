package lecture.xunit

abstract class TestCase(val methodName: String) {

    fun run() {
//      val method = this.javaClass.getMethod(methodName)
        val method = this::class.java.getMethod(methodName)
        method.invoke(this)
    }
}