package com.study.racingcar

import com.study.racingcar.infra.ConsoleReader

fun main() {
    val reader=  ConsoleReader()
    RacingCarGameApplication(reader, reader).run()
}