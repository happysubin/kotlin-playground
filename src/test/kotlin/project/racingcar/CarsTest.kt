package project.racingcar

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CarsTest {

    @Test
    @DisplayName("여러 차들이 이동")
    fun test1() {
        val cars = Cars(listOf(
            Car("car1", Position(0)),
            Car("car2", Position(0)),
            Car("car3", Position(0)))
        )


        cars.move(object : CarMoveService {
            override fun move(car: Car) {
                car.move(1)
            }
        })


        assertThat(cars.cars[0]).isEqualTo(Car("car1", Position(1)))
        assertThat(cars.cars[1]).isEqualTo(Car("car2", Position(1)))
        assertThat(cars.cars[2]).isEqualTo(Car("car3", Position(1)))
    }
}