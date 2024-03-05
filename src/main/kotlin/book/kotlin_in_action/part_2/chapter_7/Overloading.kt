package book.kotlin_in_action.part_2.chapter_7

import java.math.BigDecimal

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

operator fun Point.minus(other: Point): Point {
    return Point(x - other.x, y - other.y)
}

/**
 * plus, minus, mod, div,. times 모두 연산자 오버로딩 가능
 * 코틀린 연산자가 자동으로 a op b == b op a 라는 교환 법칙을 지원하지 않는다.
 */


operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt() )
}

operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}

/**
 * 단항 연산자.
 * 아래는 오버로딩할 수 있는 단항 산술 연산자
 * unaryMinus, unaryPlus, not, inc, dec
 */
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

operator fun BigDecimal.inc() = this + BigDecimal.ONE


fun main() {
    val p1 = Point(10 ,20)
    val p2 = Point(30 ,40)
    println(p1 + p2)
    println(p2 - p1)

    println(p1 * 1.5)
    println('a' * 3)

    /**
     * 코틀린은 =, -=와 같은 복합 대입 연산자도 지원한다.
     * 반환 타입이 Unit인 plusAssign 함수를 정의하면 코틀린은 += 연산자에 그 함수를 사용한다.
     *
     * 이론적으로 코드에 있는 +=를 plus와 plusAssign 양쪽으로 컴파일할 수 있다.
     * 어떤 클래스가 이 두함수를 모두 정의하고 둘 다 +=에 사용 가능한 경우 컴파일러는 오류를 보고한다.
     * 일반 연산자를 사용해 이를 해결할 수 있다.
     * plus와 plusAssignd을 동시에 정의하지말자!!!!!!!!!!!!!!!!!!!!!!
     *
     * 클래스가 앞에서 본 Point처럼 변경 불가능하다면 plus와 같이 새로운 값을 반환하는 연산을 추가해야 한다.
     */

    var p3 = Point(1, 2)
    p3 += Point(2,1)
    println("p3 = ${p3}")

    println(-p3)

    var bd = BigDecimal.ZERO
    println(bd++)
    println(++bd)

}

/**
 * 코틀린이 == 연산자 호출을 equals 메서드 호출로 컴파일한다는 점을 기억하자.
 * != 연산자를 사용해도 equals 호출로 컴파일된다.
 * 위두 연산자는 인자가 널인지도 검사한다.
 *
 * ===는 동일성을 검사. 오버로딩 불가
 * equals는 확장함수르 정으 ㅣ불가능.
 */