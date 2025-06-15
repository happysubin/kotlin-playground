package com.study.inflearn.coroutine

fun main() {
    println("START")
    newRoutineV1()
    println("END")
}

fun newRoutineV1() {
    val num1 = 1
    val num2 = 2
    println(num1 + num2)
}