package project.racingcar.presentation

import project.racingcar.domain.Cars

interface CarsReader {
    fun readCars(): Cars
}