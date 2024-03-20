package lecture.coroutine

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield


fun main() = runBlocking { //앞 세계와 코루틴 세계를 연결. runBlocking 내부부터 코루틴. 코루틴 1개
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