package com.study.numberbaseball

import com.study.numberbaseball.infra.ConsoleReader
import com.study.numberbaseball.infra.RandomBallsFactoryImpl

fun main() {
    val app = BaseBallApplication(
        ConsoleReader(),
        RandomBallsFactoryImpl()
    )
    app.start()
}