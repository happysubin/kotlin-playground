package com.study.kotlin_in_action.part_1.chapter_4

abstract class Animated {
    abstract fun animate() //반드시 오버라이드해야함.

    open fun stopAnimating() {}

    fun animateTwice() {}

}


//참고로 인터페이스는 항상 열려 있다. 늘 open 상태