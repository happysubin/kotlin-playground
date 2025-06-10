package com.study.kotlin_in_action.part_1.chapter_6.collection

/**
 * 코틀린 컬렉션이 자바 라이브러리를 바탕으로 만들어졌고, 다양한 확장 함수를 통해 기능을 추가했다
 */

fun main() {
    val l1: List<Int?> = listOf(1, 2,3)// List가 항상 널은 아니지만 요소가 널일 수도
    val l2: List<Int> = listOf(1, 2,3)// List가 널일 수도, 요소는 항상 널이 아님

    val l3: List<Int?> = listOf(1, null, 2, 3, 4, 5,)
    val l4: List<Int> = l3.filterNotNull()
    println(l4)

    /**
     * Collection에는 추가하거나 삭제하는 메서드가 없다.
     * 컬렉션을 수정하려면 MutableCollection 인터페이스를 사용해야 한다.
     *
     * 자바 클래스를 코틀린에서 확장하거나 자바 인터페이스를 코틀린에서 구현하는 경우 메서드 파라미터의 널 가능성과 변경 가능성에 대해 깊이 생각해봐야 한다.
     */

    /**
     * 컬렉션 인터페이스를 사용할 때 항상 염두에 둬야 할 핵심은 읽기 전용 컬렉션이라고 해서 꼭 변경 불가능한 컬렉션일 필요는 없다는 점이다.
     * 읽기 전용 인터페이스 타입인 변수를 사용할 때 그 인터페이스는 실제로는 어떤 컬렉션 인스턴스를 가리키는 수많은 참조 중 하나일 수도 있다.
     *
     * 따라서 읽기 전용도 항상 스레드 세이프하지 않다.
     */


    /**
     * 코틀린에서 배열의 인덱스 값의 범위에 대해 이터레이션하기 위해 array.indices 확장함수를 사용한다.
     * 배열 타입의 타입인자도 항상 객체 타입이 된다.
     * 박싱하지 않은 원시 타입의 배열이 필요하다면 IntArray와 같은 특별한 클래스를 사용해야한다.
     *
     * 코틀린의 Array 클래스는 일반 제네릭 클래스처럼 보인다. 하지만 Array는 자바 배열로 컴파일된다.
     *
     */
    val letters = Array<String>(26) {i -> ('a' + i).toString()}
    println(letters.joinToString(""))
}