package com.study.inflearn.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


fun main(): Unit = runBlocking {
//    val times = measureTimeMillis {
//        val job1 = async{ apiCall1() }
//        val job2 = async{ apiCall2() }
//        printWithThread(job1.await() + job2.await())
//    }
//    println("times = ${times}")

    val times = measureTimeMillis {
        val job1 = async{ apiCall1() }
        val job2 = async{ apiCall2(job1.await()) }
        printWithThread(job2.await())
    }
    println("times = ${times}")
}

suspend fun apiCall1(): Int {
    delay(1_000L)
    return 1
}

suspend fun apiCall2(num: Int): Int {
    delay(1_000L)
    return num + 2
}

//suspend fun apiCall1(): Int {
//    delay(1_000L)
//    return 1
//}
//
//suspend fun apiCall2(): Int {
//    delay(1_000L)
//    return 2
//}

fun ex5(): Unit = runBlocking {
    val job = async {
        3 + 5
    }
    val result = job.await()
    printWithThread(result)
}

fun ex4(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000)
        printWithThread("Job 1")
    }
    job1.join()
    val job2 = launch {
        delay(1_000)
        printWithThread("Job 2")
    }
}

fun ex3():Unit = runBlocking {
    val job = launch {
        (1..5).forEach{
            printWithThread(it)
            delay(500)
        }
    }

    delay(1_000)
    job.cancel()
}


fun ex2(): Unit = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        printWithThread("Hello launch")
    }
    delay(1_000L)
    job.start()

}
fun ex1() {
    runBlocking {// 이 함수가 코루틴 빑더
        printWithThread("START")
        launch { //launch는 반환 값이 없는 코루틴 실행
            delay(2_000L) //yield 2초 기다리고 아래 줄 실행
            printWithThread("LAUNCH END")
        }
    }
    printWithThread("END")
}