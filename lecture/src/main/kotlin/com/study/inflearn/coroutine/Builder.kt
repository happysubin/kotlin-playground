package com.study.inflearn.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.plus
import kotlin.system.measureTimeMillis

/**
 * runBlocking은 코루틴 빌더이자 runBlocking과 runBlocking안에서 만들어진 코루틴이 완료될때까지 스레드를 블락시킨다
 */
fun main() {
//    ex1()
//    ex2()
//    ex3()
//    ex4()
//    ex5()
    ex6()

}

fun ex6(): Unit = runBlocking {
    val times = measureTimeMillis {
        val job1 = async{ apiCall1() }
        val job2 = async{ apiCall2() }
        printWithThread(job1.await() + job2.await())
    }
    println("times = ${times}")

    val times2 = measureTimeMillis {
        val job1 = async{ apiCall1() }
        val job2 = async{ apiCall2(job1.await()) }
        printWithThread(job2.await())
    }
    println("times2 = ${times2}")

    val times3 = measureTimeMillis {
        val job1 = async(start = CoroutineStart.LAZY){ apiCall1() }
        val job2 = async(start = CoroutineStart.LAZY){ apiCall2() }
        printWithThread(job1.await() + job2.await()) //job1이 끝나야 job2가 시작
    }
    println("times3 = ${times3}")

    val times4 = measureTimeMillis {
        val job1 = async(start = CoroutineStart.LAZY){ apiCall1() }
        val job2 = async(start = CoroutineStart.LAZY){ apiCall2() }
        job1.start()
        job2.start()
        printWithThread(job1.await() + job2.await()) //job1이 끝나야 job2가 시작
    }
    println("times4 = ${times4}")
}


suspend fun apiCall2(num: Int): Int {
    delay(1_000L)
    return num + 2
}

suspend fun apiCall1(): Int {
    delay(1_000L)
    return 1
}

suspend fun apiCall2(): Int {
    delay(1_000L)
    return 2
}

fun ex5(): Unit = runBlocking {
    val job = async {
        3 + 5
    }
    val result = job.await() //async의 결과를 가져오는 함수
    printWithThread(result)
}

fun ex4(): Unit = runBlocking {
    val job1 = launch {
        delay(1_000)
        printWithThread("Job 1")
    }
    job1.join() //코루틴1 끝날때까지 대기
    val job2 = launch {
        delay(1_000)
        printWithThread("Job 2")
    }
}

/**
 * 출력 결과
 * [main] 1
 * [main] 2
 */
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
//            yield()
            printWithThread("LAUNCH END")
        }
    }
    printWithThread("END")
}