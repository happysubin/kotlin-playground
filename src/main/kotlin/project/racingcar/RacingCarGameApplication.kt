package project.racingcar

import project.racingcar.application.RacingCarGame
import project.racingcar.infra.RandomCarMoveService
import project.racingcar.presentation.CarsReader
import project.racingcar.presentation.GameCountReader

class RacingCarGameApplication(
    private val carsReader: CarsReader,
    private val gameCountReader: GameCountReader
) {

    fun run() {
        val cars = carsReader.readCars()
        val gameCount = gameCountReader.readCount()
        //TODO 여기서 미리 전체적으로 보여줘야한다.
        val winner = RacingCarGame(cars, RandomCarMoveService()).play(gameCount)
        println(winner)
        //TODO RacingCarGame 리턴 값으로 우승자를 판정해야 한다.
    }
}