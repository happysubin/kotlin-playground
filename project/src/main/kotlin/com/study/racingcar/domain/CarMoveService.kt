package com.study.racingcar.domain

@FunctionalInterface
interface CarMoveService {

    fun move(car: Car)
}