package com.study.racingcar.presentation

import com.study.racingcar.domain.Cars

interface CarsReader {
    fun readCars(): Cars
}