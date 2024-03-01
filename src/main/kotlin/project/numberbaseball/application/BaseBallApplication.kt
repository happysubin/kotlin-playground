package project.numberbaseball.application

import project.numberbaseball.domain.UserInputReader
import project.numberbaseball.infra.ConsoleReader
import project.numberbaseball.domain.RandomBallsFactory
import project.numberbaseball.infra.ResultViewImpl

class BaseBallApplication(
    private val userInputReader: UserInputReader,
    private val randomBallsFactory: RandomBallsFactory
) {
    fun start() {
        do {
            val computerBalls = randomBallsFactory.create()
            val game = BaseBallGame(ResultViewImpl(), ConsoleReader(), computerBalls)
            game.play()
        } while(userInputReader.getGameRestartFlag())
    }
}