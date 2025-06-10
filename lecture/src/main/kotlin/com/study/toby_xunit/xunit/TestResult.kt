package com.study.toby_xunit.xunit

class TestResult {

    private var runCount = 0
    private var errorCount = 0

    fun testStarted(){
        this.runCount++
    }

    fun getSummary(): String {
        return "${runCount} run, ${errorCount} failed"
    }

    fun testFailed() {
        this.errorCount++
    }
}
