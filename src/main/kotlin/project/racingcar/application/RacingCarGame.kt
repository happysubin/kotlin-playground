package project.racingcar.application

import project.racingcar.domain.CarMoveService
import project.racingcar.domain.Cars


class RacingCarGame(
    private val cars: Cars,
    private val carMoveService: CarMoveService
) {

    fun play(cnt: Int) {
        for(i in 1..cnt){
            cars.move(carMoveService)
        }
    }
}