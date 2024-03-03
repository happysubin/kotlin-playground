package book.kotlin_in_action.part1.chapter_5

/**
 * 코틀린은 수신 객체를 명시하지 않고 람다의 본문 안에서 다른 객체의 메서드를 호출할 수 있게 한다.
 * 그런 람다를 수신 객체 지정 람다라고 부른다.
 *
 */

fun main() {
    //with: 어떤 객체의 이름을 반복하지 않고도 다양한 연산을 수행할 수 있게 한다.
    println(alphabet())
    println(alphabetV2())
    val builder = alphabetV3()
    println("builder = ${builder}")

}

fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

fun alphabetV2(): String {
    val stringBuilder = StringBuilder()
    //결국 with은 파라미터가 2개. StringBuilder와 람다. with과 run은 매우 유사
    //with 함수는 첫 번째 인자로 받은 객체를 두 번째 인자로 받은 람다의 수신 객체로 만든다. 람다에서 this로 접근 가능
    return with(stringBuilder) {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\nNow Iappend(\"\\nNow I know the alphabet!\")\n")

        //with가 반환하는 값은 람다 코드를 실행한 결과며, 그 결과는 람다 식의 본문에 있는 마지막 식의 값이다
        //람다의 결과 대신 수신 객체가 필요한 경우도 있는데 그럴 때는 apply 라이브러리 함수를 사용한다.

        //this.toString()
        //with가 반환하는 값은 람다 코드를 실행한 결과며, 그 결과는 람다 식의 본문에 있는 마지막 식의 값이다.
        //람다의 결과 대신 수신 객체가 필요한 경우도 있는데 그럴 때는 apply 라이브러리 함수를 사용한다.
        toString()
    }
}

fun alphabetV3() = StringBuilder().apply{
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow Iappend(\"\\nNow I know the alphabet!\")\n")
}.toString()