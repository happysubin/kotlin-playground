package com.study.kotlin_in_action.part_1.chapter_6.primitive


/**
 * 코틀린은 원시 타입과 래퍼  타입을 구분하지 않는다. 항상 같은 타입을 사용한다.
 *
 * 실행 시점에 숫자 타입은 가능한 한 가장 효율적인 방식으로 표현된다.
 * 대부분의 경우 코틀린의 Int 타입은 자바 int 타입으로 컴파일 된다.
 * 이런 컴파일이 불가능한 경우는 컬렉션과 같은 제네릭 클래스를 사용하는 경우 뿐이다.
 *
 * 예를 들어 Int 타입을 컬렉션의 타입 파라미터로 넘기면 그 컬렉션에는 Int의 래퍼 타입에 해당하는 java.lang.Integer 객체가 들어간다.
 *
 * Int와 같은 코틀린 타입에는 널 참조가 들어가 수 없기 때문에 쉽게 그에 상응하는 자바 원시타입으로 컴파일할 수 있다.
 * 마찬가지로 반대로 자바 원시 타입의 값은 겿코 널이 될 수 없으므로 자바 원시 타입을ㅊ 코틀린에서 사용할 때도 널이 될 수 없는 타입으로 취급할 수 있다.
 *
 * 코틀린에서 널이 될 수 있는 원시 타입을 사용하면 그 타입은 자바의 래퍼 타입으로 컴파일 된다.
 *
 *
 */

data class PersonV9(val name: String, val age: Int? = null) {
    fun isOlderThan(other: PersonV9): Boolean? {
        if(age == null || other.age == null) return null
        return age > other.age
    }
}


fun main() {
    println(PersonV9("Sam", 35).isOlderThan(PersonV9("Amy", 42)))

    /**
     * Any 타입은 모든 널이 될 수 없는 타입의 조상 타입
     * 내부에서 Any 타입은 java.lang.Object에 해당.
     *
     * 코틀린 Unit 타입은 자바 void와 같은 기능을 한다.
     * void와 Unit의 차이점이 뭘까?
     * Unit은 모든 기능을 갖는 일반적인 타입이며, void와 달리 Unit을 타입인자로 사용할 수 있다.
     * 함수형 프로그래밍에서 전통적으로 Unit은 '단 하나의 인스턴스만 갖는  타입'을 의미해 왔다.
     *
     * 정상적으로 끝나지 않는 함수의 반환 타입을 지정할 때 Nothing 사용한다.
     * 함수의 리턴 값이 없거나, 예외를 던지는 함수의 리턴 타입에 정의 or Null을 리턴하는 함수에 정의한다.
     */
}

