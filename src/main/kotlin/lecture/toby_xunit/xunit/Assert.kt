package lecture.toby_xunit.xunit

object Assert {
    fun assertEquals(expected: Any, actual: Any) {
        if(!expected.equals(actual)) throw AssertionError("expected: $expected, actual: $actual")
    }
}