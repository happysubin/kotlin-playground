package com.study.kotlin_in_action.part_1.chapter_4

class Button: Clickable, Focusable {

    //오버라이드 변경자를 꼭 사용해야 한다.
    override fun click() = println("i was clicked")

    //구현한 인터페이스의 동일한 시그니처의 메서드가 있으면 아래와 같이 해결해야함.
    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }


    //자바와는 달리 default를 안붙여도 됨.
    //override fun showOff() = println("off")

}

fun main() {
    val button = Button()
    button.click()
    button.setFocus(true)
    button.showOff()
}