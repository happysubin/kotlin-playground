package com.study.numberbaseball

import com.study.numberbaseball.application.BaseBallGame
import com.study.numberbaseball.presentation.UserInputReader
import com.study.numberbaseball.infra.ConsoleReader
import com.study.numberbaseball.domain.RandomBallsFactory
import com.study.numberbaseball.infra.ResultViewImpl

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