package project.racingcar.application

import project.racingcar.domain.CarMoveService
import project.racingcar.domain.Cars
import project.racingcar.domain.Winner


class RacingCarGame(
    private val cars: Cars,
    private val carMoveService: CarMoveService
) {

    fun play(cnt: Int): String {
        for(i in 1..cnt){
            cars.move(carMoveService)
        }
        return Winner.extract(cars.cars)
    }
}