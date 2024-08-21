package book.kotlin_in_action.part_1.chapter_4.nested

sealed class ExprV2 {
    class Num(val value: Int): ExprV2()
    class Sum(val left: ExprV2, val right: ExprV2) : ExprV2()
}

fun eval(e: ExprV2): Int =
    when (e) {
        is ExprV2.Num -> e.value
        is ExprV2.Sum -> eval(e.right) + eval(e.left)
    }

/***
 * sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 한다.
 * when 식에서 sealed 클래스의 모든 하위 클래스를 처리한다면 디폴트 분기가 필요 없다.
 * sealed로 표시된 클래스는 자동으로 open이 된다.
 *
 */