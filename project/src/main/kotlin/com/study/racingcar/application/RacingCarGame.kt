package com.study.racingcar.application

import com.study.racingcar.domain.CarMoveService
import com.study.racingcar.domain.Cars
import com.study.racingcar.domain.Winner


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