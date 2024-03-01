package project.numberbaseball

import project.numberbaseball.application.BaseBallApplication
import project.numberbaseball.infra.ConsoleReader
import project.numberbaseball.infra.RandomBallsFactoryImpl

fun main() {
    val app = BaseBallApplication(
        ConsoleReader(),
        RandomBallsFactoryImpl()
    )
    app.start()
}