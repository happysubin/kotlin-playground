package com.study.kotlin_in_action.part_1.chapter_4.nested

/**
 * 자바와 코틀린
 * static class = class
 * class = inner class
 *
 * 중첩 클래스안에는 바깥쪽 클래스에 대한 참조가 없지만 내부 클래스에는 있다.
 *
 * 내부 클래스 innerdk
 */

class Button: View {
    override fun getCurrentState(): State = ButtonState()

    override fun restoreState(state: State) {
        TODO("Not yet implemented")
    }

    class ButtonState: State //자바로 디컴파일하면 static final class

    inner class ButtonStateV2: State //자바로 디컴파일하면 final class
}