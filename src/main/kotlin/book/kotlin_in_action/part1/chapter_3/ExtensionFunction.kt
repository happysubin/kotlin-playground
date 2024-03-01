package book.kotlin_in_action.part1.chapter_3

/**
 * 확장함수: 어떤 클래스의 멤버 메서드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수
 * 클래스 이름을 수신 객체 타입: String
 * 호출되는 대상이 되는 값을 수신 객체: this
 *
 * String 클래스 클래스에 새로운 메서드를 추가하는 것과 같다.
 *
 * 확장 함수 안에서는 클래스 내부 에서만 사용할 수 있는 private 멤버나 protected 멤버를 사용할 수 없다.
 *
 * 확장 함수는 오버라이드 불가
 */
fun String.lastChar(): Char = this.get(this.length - 1)

//this 생략 가능
fun String.firstChar(): Char = get(0)


fun main() {
    println("Kotlin".lastChar())
    println("Kotlin".firstChar())

}