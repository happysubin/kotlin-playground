package book.kotlin_in_action.part_1.chapter_6.nullability

import java.util.*

//null이거나 null이 될 수 있는 인자를 넘기는 것은 금지되며, 혹시 그런 값을 넘기면 컴파일 시 오류가 발생한다.
fun strlen(s: String) = s.length

//널과 문자열을 인자로 받을 수 있게 하려면 물음표를 명시해야한다.
//널이 될 수 있는 타입의 변수가 있다면 그에 대해 수행할 수 있는 연산이 제한된다.
//널이 될 수 있는 타입의 변수에 대해 변수.메서드()처럼 메서드를 직접호출할 수는 없다.
fun strlen(s: String?) = s?.length

fun printAllCaps(s: String?) {
    val allCaps: String? = s?.uppercase(Locale.getDefault())
    println(allCaps)
}

class Employee(val name: String, val manager: Employee?)

fun managerName(employee: Employee): String ? = employee.manager?.name

class Address(val streetAddress: String, val zipcode: Int, val city: String, val country: String)

data class Company(val name: String, val address: Address?)

class Person(val name: String, val company: Company?) {
    override fun equals(o: Any?): Boolean {
        //as?는 값을 대상 타입으로 변환할 수 없으면 Null을 반환한다.
        val otherPerson = o as? Person ?: return false //안전한 타입 캐스트

        return otherPerson.name == o.name && otherPerson.company == company
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (company?.hashCode() ?: 0)
        return result
    }
}

fun Person.countryName(): String {
    return company?.address?.country ?: "Unknown"
    //val country = this.company?.address?.country
    //return if (country != null) country else "Unknown"
    //return country.toString()
}

//s가 널이 아니면 좌항 값. 널이라면 "", 우항 값을 리턴

fun foo(s: String?) {
    val t: String = s ?: ""
}

fun strLenSafe(s: String?): Int = s?.length ?: 0


fun printShippingLabel(person: Person) {
    val address = person.company?.address ?: throw IllegalArgumentException("no address") //엘비스 연산자를 통해 예외 던지기도 가능
    with(address) {
        println(streetAddress)
        println("$zipcode $city $country")
    }
}

fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(sNotNull.length)
}

fun sendEmailTo(email: String) {
    println(email)
}


fun verifyUserInput(input: String?) {
    if(input.isNullOrBlank()) {
        println("Please fill in the required fields")
    }
}

fun String?.isNullOrBlank(): Boolean = this == null || this.isBlank()

//T에 대해 추론한 타입은 널이 될 수 있는 Any?
fun <T> printHashCode(t: T) {
    println(t?.hashCode())
}

//T에 대해 추론한 타입은 널이 될 수 없는 Any

fun <T: Any> printHashCodeV2(t: T) {
    println(t?.hashCode())
}

fun main() {
    println(strlen(null.toString()))
    println(strlen(null))

    /**
     * 널이 될 수 있는 값을 널이 될 수 없는 변수에 대입할  수 없다.
     * 널이 될 수 있는 타입의 값을 널이 될 수 없는 타입의 파라미터를 받는 함수에 전달할 수 없다.
     *
     * 코틀린이 제공하는 가장 유용한 도구 중 하나가 안전한 호출 연산자인 ?.이다.
     * 호출하려는 값이 null이 아니라면 ?.은 일반 메서드 호출처럼 작동한다.
     */
    printAllCaps("ABC")
    printAllCaps(null)

    val ceo = Employee("Da Boss", null)
    val developer = Employee("Bob Smith", ceo)
    println(managerName(developer))
    println(managerName(ceo))

    val person = Person("Dmitry", null)
    println(person.countryName())

    /**
     * 코틀린은 null 대신 사용할 디폴트 값을 지정할 때 편리하게 사용할 수 있는 연산자를 제공한다. -> 엘비스 연산자 ?:
     */
    println(strLenSafe("ABCD"))
    println(strLenSafe(null))

    println()
    val address = Address("Elsetr. 47", 80687, "Munich", "Germany")
    val jetbrains = Company("JetBrains", address)
    val jetbrains2 = Company("JetBrains", address)

    val dmitry = Person("Dmitry", jetbrains)
    printShippingLabel(dmitry)

    println()
    val p1 = Person("Dmitry", jetbrains)
    val p2 = Person("Dmitry", jetbrains2)
    println(p1 == p2)

    println(p1.equals(42))


    /**
     * 널 아님 단언은 코틀린에서 널이 될 수 있는 타입의 값을 다룰 대 사용할 수 있는 도구 중에서 가장 단순하면서 무딘 도구다.
     * 느낌표를 이중(!!)으로 사용하면 어떤 값이든 널이 될 수 없는 타입으로(강제로) 바꿀 수 있다.
     * 실네 널에 대해 !!를 적용하면 NPE가 발생한다.
     */
    //ignoreNulls(null) //NPE는 !!를 사용하는 곳에서 발생한다. 꼭 필요한 경우에만 사용하자.

    /**
     * 널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘기려면 어떻게 해야할까?
     * 그런 호출은 안전하지 않으므로 컴파일러는 그호출을 허용하지 않는다.
     * 코틀린 언어는 이런 경우 특별한 지원을 제공하지 않지만 표준 라이브러리에 도움디 될 수 있는 함수가 있다.
     * 그 함수는 let 이다.
     *
     * 하지만 let 함수를 통해 인자를 전달할 수도 있다.
     * let 함수는 자신의 수신 객체를 인자로 전달받은 람다에게 넘긴다.
     * 널이 될 수 있는 값에 대해 안전한 호출 구문을 사용해 let을 호출하되 널이 될 수 없는 타입을 인자로 받는 람다를 let에 전달한다.
     */
    val e1: String? = "123@gmail.com"
    //sendEmailTo(e1) 에러

    //겁나 좋은 방법..
    val e2: String? = "123@gmail.com"
    e2?.let {
        sendEmailTo(it)
    }

    /**
     * lazyinit 변경자를 붙이면 프로퍼티를 나중에 초기화할 수 있다.
     * var이여야 한다. 원시타입에는 사용할 수 없다.
     * 늦은 초기화조차 하지 않으면 컴파일 단계에서 오류가 발생
     */



    verifyUserInput(" ")
    verifyUserInput(null)

    println("123".isNullOrBlank())
    println(null.isNullOrBlank())

    println()
    printHashCode(null)
    printHashCodeV2("123")
    //printHashCodeV2(null) 컴파일 에러


    /**
     * 플랫폼 타입은 코틀린이 널 관련 정보를 알 수 없는 타입을 말한다.
     * 그 타입을 널이 될 수 있는 타입으로 처리해도 되고 널이 될 수 없는 타입으로 처리해도 된다.
     * 플랫폼 타입에 대해 수행하는 모든 연산에 대한 책임은 온전히 우리에게 있다.
     *
     * 자바 API를 다룰 때는 조심하자. 대부분의 라이브러리는 널 관련 애노테이션을 사용하지 앟ㄴ는다.
     *
     * 코틀린 컴파일러가 표시한 String! 이라는 타입은 자바 코드에서 온 플랫폼 타입이다.
     * String! 타입의 널 가능성에 대해 아무 정보도 없다는 뜻이다.
     * 자바에서 가져온 타입은 코틀린에서 플랫폼 타입으로 취급되는 것!!!!!!!
     */

}