package project.numberbaseball.application

import project.numberbaseball.domain.Balls
import project.numberbaseball.domain.BallsReader
import project.numberbaseball.domain.Result
import project.numberbaseball.domain.ResultView

class BaseBallGame(
    private val resultView : ResultView,
    private val ballsReader: BallsReader,
    private val gameBalls: Balls
) {
    fun play() {
        do {
            val playerBalls = ballsReader.read()
            val result = gameBalls.play(playerBalls)
            resultView.print(result)
        } while (Result.isContinue(result))
    }
}