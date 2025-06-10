package com.study.toby_xunit.xunit

import com.study.toby_xunit.xunit.annotation.TestAnnotation

class TestCaseTest(methodName: String): TestCase(methodName) {

    var wasRun: WasRun? = null

    companion object {
        fun suite(): TestSuite {
            return TestSuite(TestCaseTest::class.java)
        }
    }

    @TestAnnotation
    fun testTemplateMethod()  {
        wasRun = WasRun("testMethod")
        val result = TestResult()
        wasRun!!.run(result)
        Assert.assertEquals("setUp testMethod tearDown", wasRun!!.log!!)
    }

    @TestAnnotation
    fun testResult() {
        wasRun = WasRun("testMethod")
        val result = TestResult()
        wasRun!!.run(result)
        Assert.assertEquals("1 run, 0 failed", result.getSummary())
    }

    @TestAnnotation
    fun failedResultFormatting() {
        val result = TestResult()
        result.testStarted()
        result.testFailed()
        Assert.assertEquals("1 run, 1 failed", result.getSummary())
    }

    @TestAnnotation
    fun testFailedResult() {
        wasRun = WasRun("testBrokenMethod")
        val result = TestResult()
        wasRun!!.run(result)
        Assert.assertEquals("1 run, 1 failed", result.getSummary())
    }

    @TestAnnotation
    fun testSuite() {
        val testSuite = TestSuite()
        testSuite.add(WasRun("testMethod"))
        testSuite.add(WasRun("testBrokenMethod"))
        val result = TestResult()
        testSuite.run(result)
        Assert.assertEquals("2 run, 1 failed", result.getSummary())
    }
}