package com.study.kotlin_in_action.part_1.chapter_3




fun <T> joinToString(
    collection: Collection<T>,
    separator: String =",",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for((index ,element) in collection.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun <T> Collection<T>.joinToStringV2(
    separator: String =",",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for((index ,element) in this.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

var opCount = 0

fun performOperation() {
    opCount++
}

fun main() {
    val list = listOf(1, 2, 3)
    println(joinToString(list, separator = ";",prefix = "(", postfix = ")"))

    println(joinToString(list, ";"))
    println(joinToString(list))

    performOperation()
    println(opCount)

    println("Kotlin".lastChar())
}