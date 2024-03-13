package project.racingcar.domain

@FunctionalInterface
interface CarMoveService {

    fun move(car: Car)
}