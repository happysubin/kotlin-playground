package com.study.kotlin_in_action.part_1.chapter_4.nested

interface Expr

class Num(val value: Int): Expr

class Sum(val left: Expr, val right: Expr): Expr

fun eval (e: Expr): Int =
    when (e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> throw IllegalArgumentException("Unknown expression")
    }