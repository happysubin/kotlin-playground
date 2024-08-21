package book.kotlin_in_action.part_1.chapter_5

/**
 * map이나 filter 같은 컬렉션 함수는 결과 컬렉션을 즉시 생성한다.
 * 컬렉션 함수를 연쇄하면 매 단계마다 계산 중간 결과를 새로운 컬렉션에 임시로 담는다는 말이다.
 * 시퀀스를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 인쇄할 수 있다.
 *
 */

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))

    println(people.map(Person::name).filter { it.startsWith("A") })

    /**
     * 더 효율적으로 만들기위해 원본 컬렉션을 시퀀스로 변환해서 사용하자. 중간 컬렉션이 생기지 않음. 지연연산 ok
     */

    val l = people.asSequence()
        .map(Person::name)
        .filter { it.startsWith("A") }
        .toList() //시퀀스를 다시 리스트로 만든다.
    println(l)

    /**
     * 시퀀스의 원소는 필요할 때 비로소 계산된다.
     * asSequence 확장 함수를 호출하면 어떤 컬렉션이든 시퀀스로 바꿀 수 있다.
     * 시퀀스를 리스트로 만들 때는 toList를 사용한다.
     *
     * 시퀀스의 원소를 차례대로 이터레이션해야 한다면 시퀀스를 직접 써도 된다.
     * 하지만 시퀀스 원소를 인덱스를 사용해 접근하는 등의 다른 API가 필요하다면 시퀀스를 리스트로 변환해야 한다.
     *
     * 시퀀스에 대한 연산을 지연 계산하기 때문에 정말 계산을 실행하게 만드려면 최종 시퀀스의 원소를 하나씩 이터레이션핟거나 최종 시퀀스를 리스트로 변환해야 한다.
     */

    //시퀀스에 대한 연산은 중간 연산과 최종 연산으로 나뉜다.

    //최종 연산이 있으므로 출력 x
    listOf(1, 2, 3, 4).asSequence()
        .map{ print("map($it)"); it * it }
        .filter { print("filter($it)"); it % 2 ==0 }

    //최종 연산이 있으므로 출력 o
    listOf(1, 2, 3, 4).asSequence()
        .map{ print("map($it)"); it * it }
        .filter { print("filter($it)"); it % 2 ==0 }
        .toList()

    println(listOf(1, 2, 3, 4)
        .asSequence()
        .map { it * it }
        .find { it > 3 }
    )

}