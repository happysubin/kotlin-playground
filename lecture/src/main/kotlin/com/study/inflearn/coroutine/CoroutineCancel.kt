package com.study.inflearn.coroutine

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
//    cancelEx1()
//    cancelExc2()
//    cancelExc3()
    cancelExc4()
}

fun cancelExc4(): Unit = runBlocking {
    val job = launch {
        try {
            delay(1000L)
        } catch (e: CancellationException) { }
        printWithThread("delay에 의해 최소되지 않았다!")
    }
    delay(100L)
    printWithThread("취소 시작")
    job.cancel()
}


fun cancelExc3(): Unit = runBlocking {
    val job = launch(Dispatchers.Default) { //코루틴을 다른 스레드에 배정 가능
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++} 번째 출력")
                nextPrintTime += 1_000L
            }
        }

        if(isActive == false) { //현재 코루틴이 활성화되어 있는가
            throw CancellationException()
        }
    }
    delay(100L)
    job.cancel()
}



fun cancelExc2(): Unit = runBlocking {
    val job = launch {
        var i = 1
        var nextPrintTime = System.currentTimeMillis()
        while (i <= 5) {
            if (nextPrintTime <= System.currentTimeMillis()) {
                printWithThread("${i++} 번째 출력")
                nextPrintTime += 1_000L
            }
        }
    }
    delay(100L)
    job.cancel()
}


/** 결과
 * [main] Job 2
 */
fun cancelEx1(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000L)
        printWithThread("Job 1")
    }

    val job2 = launch {
        delay(1_000L)
        printWithThread("Job 2")
    }

    delay(100)
    job1.cancel()
}
