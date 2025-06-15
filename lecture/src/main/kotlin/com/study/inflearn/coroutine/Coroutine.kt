package com.study.inflearn.coroutine

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

/**
 * 1. runBlocking로 인해 새로운 코루틴이 만들어진다
 * 2. start 출력
 * 3. launch로 새로운 코루틴을 만듦. 그러나 바로 실행되지는 않음
 * 4. yield()에 의해 runBlocking에 의해 만들어진 코루틴이 대기
 * 5. launch에 의해 만들어진 코루틴이 실행되면서 newRoutine를 호출
 * 6. newRoutine 내부에 yield에 의해서 해당 코루틴이 다시 디기
 * 7. end가 출력되고
 * 8. 마지막으로 num1 + num2 구문이 출력된다
 *
 * [main @coroutine#1] start
 * [main @coroutine#1] end
 * [main @coroutine#2] num1 + num2 = 3
 */
fun main(): Unit = runBlocking { //앞 세계와 코루틴 세계를 연결. runBlocking 내부부터 코루틴. 코루틴 1개
    printWithThread("start")
    launch { //반환 값이 없는 코루틴을 만든다. 현재 코루틴 2개
        newRoutine()
    }
    yield() // runBlocking 휴식 newRoutine이 실행
    printWithThread("end")
}

suspend fun newRoutine() { //suspend fun은 다른 suspend fun 호출 가능
    val num1 = 1
    val num2 = 2
    yield() //이거도 suspend yield는 코루틴을 잠깐 멈춤. 스레드를 양보한다. 그럼 Main이 실행된다.
    printWithThread("num1 + num2 = ${num1 + num2}")
}

fun printWithThread(str: Any) {
    println("[${Thread.currentThread().name}] $str")
}

//코루틴은 중단되도 다시 접근 가능. 루틴은 다시 접근 불가능