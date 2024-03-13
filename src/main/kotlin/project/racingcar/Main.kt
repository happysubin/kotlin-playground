package project.racingcar

import project.racingcar.infra.ConsoleReader

fun main() {
    val reader=  ConsoleReader()
    RacingCarGameApplication(reader, reader).run()
}