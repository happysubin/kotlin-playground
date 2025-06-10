package com.study.racingcar.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WinnerTestAnnotation {

    @Test
    @DisplayName("동일한 우승자가 2명 존재")
    fun test1() {
        val cars = listOf(
            Car("1", Position(0)),
            Car("2", Position(2)),
            Car("3", Position(2))
        )

        val result = Winner.extract(cars)

        Assertions.assertThat(result).isEqualTo("2, 3")
    }

    @Test
    @DisplayName("동일한 우승자가 3명 존재")
    fun test2() {
        val cars = listOf(
            Car("1", Position(2)),
            Car("2", Position(2)),
            Car("3", Position(2))
        )

        val result = Winner.extract(cars)

        Assertions.assertThat(result).isEqualTo("1, 2, 3")
    }

    @Test
    @DisplayName("우승자가 1명 존재")
    fun test3() {
        val cars = listOf(
            Car("1", Position(2)),
            Car("2", Position(3)),
            Car("3", Position(4))
        )

        val result = Winner.extract(cars)

        Assertions.assertThat(result).isEqualTo("3")
    }
}