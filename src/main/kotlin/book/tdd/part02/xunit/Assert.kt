package book.tdd.part02.xunit

object Assert {

    fun assertEqual(expected: Any, actual: Any) {
        if(!expected.equals(actual)) {
            throw AssertionError("expected: $expected, actual: $actual")
        }
    }
}