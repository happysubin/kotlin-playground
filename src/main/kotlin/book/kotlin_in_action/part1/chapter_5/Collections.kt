package book.kotlin_in_action.part1.chapter_5

import java.util.*


data class Book(val title: String, val authors: List<String>)

fun main() {
    val list = listOf(1, 2, 3, 4)
    println(list.filter{ it % 2 == 0})

    val people = listOf(Person("Alice", 29), Person("Bob", 31), Person("SU", 24))
    println(people.filter { it.age  > 30})

    println(list.map{ it * it })

    println(people.map(Person::name))

    //아래가 동일
    println(people.filter{it.age > 30}.map(Person::name))
    println(people.filter{it.age > 30}.map{it.name})

    //개선 시작
    println(people.filter{it.age == people.maxByOrNull (Person::age)!!.age})

    val maxAge = people.maxByOrNull(Person::age)?.age
    println(people.filter { it.age == maxAge })

    val numbers = mapOf(0 to "zero", 1 to "one")
    println(numbers.mapValues{ it.value.uppercase(Locale.getDefault()) })

    /**
     * 컬렉션에 대해 자주 수행하는 연산으로 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산이 있다
     * 코틀린에서는 all과 any가 이런 연산이다.
     * count 함수는 조건을 만족하는 원소의 개수를 반환.
     * find 함수는 조건을 만족하는 첫 번째 원소를 반환한다.
     */
    val canBeInClub27 = { p:Person -> p.age <= 27}
    println(people.all(canBeInClub27))  //모두가 27살 이하라면 True
    println(people.any(canBeInClub27))  //한명이라도 27살 이하라면 True

    val l = listOf(1, 2, 3, 4)
    println(list.any{ it == 3})
    println(people.count(canBeInClub27))

    println(people.find(canBeInClub27))

    /**
     * 컬렉션의 모든 원소를 어떤 특성에 따라 여러 그룹으로 나누고 싶다면 groupBy 함수를 사용하자.
     */
    println(people.groupBy { it.age > 30 })
    val l2 = listOf("a", "ab", "b", "c")
    println(l2.groupBy(String::first))

    /**
     * flatMap 함수는 먼저 인자로 주어진 ㄹ마다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다.
     */
    val books = listOf(Book("제목1", listOf("작가1", "작가2")), Book("제목2", listOf("작가2", "작가3")))
    println(books.flatMap { it.authors }.toSet()) //[작가1, 작가2, 작가3]

    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })

}