package com.study.inflearn.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 코루틴의 예외 처리와 Job의 상태 변화
 */

fun main() {
//    stateEx1()
//    stateEx2()
//    stateEx3()
//    stateEx4()
//    stateEx5()
    stateEx6()
}

fun stateEx1(): Unit = runBlocking {
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        delay(1000)
        printWithThread("Job 1")
    }

    val job2 = CoroutineScope(Dispatchers.Default).launch {
        delay(1000)
        printWithThread("Job 2")
    }
}


fun stateEx2(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).launch {
        throw IllegalArgumentException("")
    }
    delay(1_000L)
}


fun stateEx3(): Unit = runBlocking {
    val job = CoroutineScope(Dispatchers.Default).async {
        throw IllegalArgumentException("")
    }
    delay(1_000L)
    job.await()
}

fun stateEx4(): Unit = runBlocking {
    val job = async {
        throw IllegalArgumentException("")
    }
    delay(1_000L)
}

fun stateEx5(): Unit = runBlocking {
    val job = async(SupervisorJob()) {
        throw IllegalArgumentException("")
    }
    delay(1_000L)
//    job.await()
}

fun stateEx6(): Unit = runBlocking {
    val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        printWithThread("예외")
    }
    val job = CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
        throw IllegalArgumentException()
    }
    delay(1_000L)
}