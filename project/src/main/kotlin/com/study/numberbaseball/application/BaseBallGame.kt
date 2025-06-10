package com.study.numberbaseball.application

import com.study.numberbaseball.domain.Balls
import com.study.numberbaseball.domain.BallsReader
import com.study.numberbaseball.domain.Result
import com.study.numberbaseball.domain.ResultView

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