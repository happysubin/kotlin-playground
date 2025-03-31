package book.tdd.part02.xunit

class TestResult {

    private var runCount: Long = 0
    private var errorCount: Long = 0

    fun testStarted() {
        this.runCount++
    }

    fun testFailed() {
        this.errorCount++
    }

    fun getSummary(): String {
        return "$runCount run, $errorCount failed"
    }
}
