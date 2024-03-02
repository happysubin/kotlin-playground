package book.kotlin_in_action.part1.chapter_4


/**
 * 어떤 클래스의 상속을 허용하려면 클래스 앞에 open 변경자를 붙여야 한다.
 * 오버라이드를 허용하고 싶은 메서드나 프로퍼티의 앞에도 open 변경자를 붙여야 한다.
 * 코틀린의 클래스와 메서드는 기본적으로 final이기 때문
 */
open class RichButton: Clickable {

    fun disable() {}

    open fun animate() {}

    //자식 클래스에서 더는 오버라이드 하지못하게 final 붙임
    final override fun click() {
        TODO("Not yet implemented")
    }
}