package com.study.racingcar.domain

class Cars(
    private val _cars: List<Car>
) {

    fun move(carMoveService: CarMoveService) {
        _cars.forEach{ carMoveService.move(it) }
        //TODO 이것도 수정하면 좋을 듯
        println()
    }

    val cars: List<Car>
        get() = _cars
}