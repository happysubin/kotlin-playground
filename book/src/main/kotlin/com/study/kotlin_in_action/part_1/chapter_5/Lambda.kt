package com.study.kotlin_in_action.part_1.chapter_5

import java.awt.Button

/**
 * 람다식 또는 람다는 기본적으로 다른 함수에 넘길 수 있는 작은 코드를 의미.
 *
 * 람다는 값처럼 여기저기 전달할 수 있는 동작의 모음이다.
 * 람다를 선언해서 따로 변수에 저장도 가능하다. 함수에 인자로 넘기면서 바로 람다를 정의하는 경우가 대부분.
 *
 * 코틀린 람다식은 항상 중괄호로 둘러쌰여 있다.
 * 인자 목록 주변에 괄호가 없다는 사실을 꼭 기억하자. 화살표가 인자 목록과 람다 본문을 구분해준다.
 *
 */


data class Person(val name: String, val age: Int)

fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if(person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}

//코틀린에서는 자바와 달리 람다에서 람다 밖 함수에 있는 파이널이 아닌 변수에 접근할 수 있다.
//기본적으로 함수 안에 정의된 로컬 변수의 생명주기는 함수가 반환되면 끝난다.
//하지만 어떤 함수가 자신의 로컬 변수를 포획한 람다를 반환하거나 다른 변수에 저장한다면 로컬 변수의 생명주기와 함수의 생명주기가 달라질 수 있다.
//포획한 변수가 있는 람다를 저장해서 함수가 끝난 뒤에 실행해도 람다의 본문 코드는 여전히 포획한 변수를 읽거나 쓸 수 있따.
//파이널 변수를 포획한 경우에는 람다 코드를 변수 값과 함께 저장한다.
//파이널이 아닌 변수를 포획한 경우에는 변수를 특펼한 래퍼로 감싸서 나중에 변경하거나 읽을 수 있게 한다음, 래퍼에 대한 ㅇ참조를 람다 코드와 함께 저장한다.

fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if(it.startsWith("4")) {
            clientErrors++
        } else if(it.startsWith("5")) {
            serverErrors++ //람다 바깥의 변수를 변경한다.
        }
    }
    println("client Errors: $clientErrors, server Errors: $serverErrors")
}

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    //정의한 함수 사용
    findTheOldest(people)

    //람다 사용
    println(people.maxByOrNull { it.age })
    println(people.maxByOrNull(Person::age))

    val sum = {x: Int, y: Int -> x + y}
    println(sum(1, 2))
    run{ println(42) }

    /**
     * 개선 단계
     */
    println(people.maxByOrNull( {p: Person -> p.age}))
    //람다가 유일한 인자이고 마지막 인자다. 따라서 괄호 뒤에 람다를 둘 수 있다.
    println(people.maxByOrNull() { p:Person -> p.age })
    //람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다,.
    println(people.maxByOrNull { p: Person -> p.age })

    //인자가 여럿 있는 경우에는 람다를 밖으로 빼낼 수도 있고 람다를 괄호 안에 유지해서 함수의 인자임을 분명히 할 수도 있다.
    //둘 이상의 람다를 인자로 받는 함수라고 해도 인자 목록의 맨 마지막 람다만 밖으로 뺄 수 있다.


    //코틀린에서는 함수 호출 시 마지막 인자가 람다 식인 경우, 해당 람다 식을 함수 호출 괄호 밖으로 뺄 수 있습니다
    val name = people.joinToString(separator = " ", transform = {p: Person -> p.name})
    println(name)

    val name2 = people.joinToString(", ") {p: Person -> p.name}
    println(name2)

    //파라미터 타입 생략. 컴파일러가 추론
    println(people.maxByOrNull{ p -> p.age })



    //람다의 파라미터 이름을 디폴트 이름인 it으로 바꾸면 람다 식을 더 간편하게 사용할 수 있다.
    //람다의 파라미터가 하나뿐이고 그 타입을 컴파일러가 추론할 수 있는 경우 it을 바로 쓸 수 있다.
    //람다 파라미터 이름을 따로 지정하지 않은 경우에만 it라는 이름이 자동으로 만들어진다.
    println(people.maxByOrNull { it.age })

    /**
     * 람다 안에 람다가 중첩되는 경우 각 람다의 파라미터를 명시하는 편이 낫다.
     */


    //람다를 변수에 저장할 때는 파라미터 타입을 추론할 문맥이 존재하지 않는다. 따라서 파라미터 타입을 명시해야 한다.
    val getAge = {p:Person -> p.age}
    people.maxByOrNull(getAge)



    val sum2 = {x:Int, y:Int ->
        println("asdf")
        x + y
    }
    val sum3 = sum2(2, 3)
    println(sum3)

    val errors = listOf("403 Forbidden", "404 Not Found")
    printMessageWithPrefix(errors, "Error: ")

    //코틀린 람다 안에서는 파이널 변수가 아닌 변수에 접근할 수 있다는 점이다.
    //또한 람다 안에서 바깥의 변수를 변경해도 된다.

    val responses = listOf("200 OK", "418 I'm a teapot", "500 Internal Server Error")
    printProblemCounts(responses)

    /**
     * 람다를 이벤트 핸들러나 다른 비동기적으로 실행되는 코드로 활용하는 경우 함수 호출이 끝난 다음에 로컬 변수가 변경될 수도 있다.
     */

    fun tryToCountButtonClicks(button: Button) {
        //var clicks = 0
        //button.onClick{clicks++}
        //return clicks -> 항상 0을 반환한다.
    }

    val getAge2 = {person:Person -> person.age}
    val getAge3 = {Person::age}

    run(::salute)
    println(Object::class)
}

fun salute() = println("Salute!")
