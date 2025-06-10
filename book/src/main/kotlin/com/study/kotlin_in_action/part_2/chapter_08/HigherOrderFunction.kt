package com.study.kotlin_in_action.part_2.chapter_08

import java.util.*

/**
 * 고차 함수는 다른 함수를 인자로 받거나 함수를 반환하는 함수다.
 * 고차함수는 람다나 함수 참조를 인자로 넘길 수 있거나 람다나 함수 참조를 반환하는 함수다.
 *
 * 함수 타입을 정의하려면 함수 파라미터의 타입을 괄호 안에 넣고, 그 뒤에 화살표를 추가한다음, 함수의 반환 타입을 지정하면 된다.
 */

fun twoAndThree(operations: (Int, Int) -> Int) {
    val result = operations(2, 3)
    println(result)
}

fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for(index in 0 until length) {
        val element = get(index)
        if(predicate(element)) sb.append(element)
    }
    return sb.toString()
}

fun <T> Collection<T>.joinToString(
    separator: String =",",
    prefix: String = "",
    postfix: String = "",
    transform: ((T) -> String)? = { it.toString() }
): String {
    val result = StringBuilder(prefix)
    for((index, element) in this.withIndex()) {
        if(index > 0) result.append(separator)
        //result.append(transform(element))
        val str = transform?.invoke(element) ?: element.toString()
    }
    result.append(postfix)
    return result.toString()
}

enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

//함수를 리턴
fun getShippingCostCalculator(
    delivery: Delivery
): (Order) -> Double {
    if(delivery == Delivery.EXPEDITED) {
        return {order -> 6 + 2.1 * order.itemCount}
    }
    return { order -> 1.2 * order.itemCount }
}

data class Person(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?
)

class ContactListFilters {
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate() : (Person) -> Boolean {
        val startsWithPrefix = { p: Person ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if(!onlyWithPhoneNumber) {
            return startsWithPrefix
        }

        return {
            startsWithPrefix(it) && it.phoneNumber != null
        }
    }
}

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS { WINDOWS, LINUX, IOS, ANDROID, MAC }

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID),
)

val averageWindowsDuration = log
    .filter { it.os == OS.WINDOWS }
    .map(SiteVisit::duration)
    .average()

fun List<SiteVisit>.averageDurationFor(os: OS) = filter{ it.os == os }.map(SiteVisit::duration).average()

val averageMobileDuration = log.filter { it.os in setOf(OS.IOS, OS.ANDROID)}.map(SiteVisit::duration).average()

fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) = filter(predicate).map(SiteVisit::duration).average()

fun main() {
    twoAndThree{ a, b -> a + b}
    twoAndThree{ a, b -> a - b}

    println("abcde".filter{
        it in 'a'..'z'
    })

    val letters = listOf("Alpha", "Beta")
    println(letters.joinToString { it.lowercase(Locale.getDefault()) })

    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
    println(calculator(Order(3)))

    val contacts = listOf(Person("Dmitry", "Jemerov", "123-4567"),
        Person("Svetlana", "Isakova", null))
    val contactListFilters = ContactListFilters()

    with(contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    println(contacts.filter(contactListFilters.getPredicate()))

    println()

    println(averageWindowsDuration)

    println(log.averageDurationFor(OS.WINDOWS))
    println(log.averageDurationFor(OS.LINUX))

    println("averageMobileDuration = ${averageMobileDuration}")

    println(log.averageDurationFor { it.os in setOf(OS.ANDROID, OS.IOS) })
    println(log.averageDurationFor { it.os == OS.IOS && it.path == "/signup"})

    //inline을 통해 람다의 성능을 개선

}
