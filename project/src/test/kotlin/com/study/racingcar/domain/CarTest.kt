package com.study.racingcar.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import com.study.racingcar.domain.Car
import com.study.racingcar.domain.Position

class CarTest {

    @Test
    @DisplayName("자동차가 2칸을 이동함.")
    fun test1() {
        val car = Car("car1", Position(0))

        car.move(2)

        assertThat(car.position).isEqualTo(Position(2))
    }
}