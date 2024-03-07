package book.kotlin_in_action.part_2.chapter_08

data class Persons
    (val name: String, val age: Int)


fun lookForAlice(people: List<Persons>) {
    for(person in people) {
        if(person.name == "Alice") {
            println("Found!")
            return
        }
    }
}

/**
 * 람다안에서 return을 사용하면 람다로부터만 반환되는게 아니라 그 람다를 호출하는 함수가 실행을 끝내고 반환된다.
 * 그렇게 자신이 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 return문을 넌로컬(non-local) return 이라고 부른다,
 *
 * return이 바깥쪽 함수를 반환히킬 수 있는 때는 람다를 인자로 받는 함수가 인라인 함수인 경우뿐이다.
 * forEach는 인라인함수이므로 람다 본문과 함께 인라이닝된다.
 * 따라서 return 식이 바깥쪽 함수(lookForAliceV2)를 반환시키도록 쉽게 컴파일할 수 있다.
 * 하지만 인라이닝되지 않는 함수에 전달되는 람다안에서 return을 사용할수는 없다.
 * 인라이닝되지않는  함수는 람다를 변수에 저장할 수 있고, 바깥쪽 함수로부터 반한된 뒤에 저장해 둔 람다가 호출될 수도 있다.
 * 그런 경우 람다안의 return이 실행되는 시점이 바깥쪽 함수를 반환시키기엔 너무 늦은 시점일수도 있다.
 */
fun lookForAliceV2(people: List<Persons>) {
    people.forEach {
        if(it.name == "Alice") {
            println("Found!")
            return //lookForAliceV2에서 반환된다.
        }
    }
    println("Alice is not found")
}

/**
 * 람다식에서도 로컬 return을 사용할 수 있다.
 * 람다 안에서 로컬 return은 for 루프의 break와 비슷한 역할을 한다
 * 로컬 return은 람다의 실행을 끝내고 람다를 호출했던 코드의 실행을 계속 이어간다.
 *
 * 로컬 return과 넌로컬 return을 구분하기 위해서는 레이블(label)을 사용해야 한다.
 * return으로 실행을 끝내고 싶은 람다 식 앞에 레이블을 붙이고, return 뒤에 그 레이블을 추가하면 된다.
 */
fun lookForAliceV3(people: List<Persons>) {
    people.forEach label@{
        if(it.name == "Alice") return@label
    }
    println("Alice might be sxomewhere")
}

//아래와 같이 레이블 사용 가능
fun lookForAliceV4(people: List<Persons>) {
    people.forEach {
        if(it.name == "Alice") return@forEach
    }
    println("Alice might be sxomewhere")
}

/**
 * 넌로컬 반환문은 장황하고, 람다 안의 여러 위치에 return 식이 들어가야 하는 경우 사용하기 불편하다.
 * 코틀린은 코드 블록을 여기저기 전달하기 위한 다른 해법을 제공하며, 그 해법을 사용하면 넌로컬 반환문을 여럿 사용해야 하는 코드 블록을 쉽게 작성할 수 있다.
 * 무명 함수가 바로 그 해법이다.
 */

fun lookForAliceV5(people: List<Persons>) {
    people.forEach(fun(person) {
        if(person.name == "Alice") return
        println("${person.name} is not Alice")
    })
}

fun main() {
    val people = listOf(Persons("Alice", 29), Persons("Bob", 31))
    lookForAlice(people)
    lookForAliceV2(people)
    lookForAliceV5(people)

    people.filter(fun(person): Boolean {
        return person.age < 30
    })

    people.filter(fun(person) = person.age < 30)
}
