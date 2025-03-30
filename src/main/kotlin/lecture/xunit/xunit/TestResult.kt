package lecture.xunit.xunit

class TestResult {

    var runCount = 0
    var failedCount = 0

    fun testStarted(){
        this.runCount++
    }

    fun getSummary(): String {
        return "${runCount} run, ${failedCount} failed"
    }
}
