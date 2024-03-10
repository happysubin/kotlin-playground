package project.racingcar

import kotlin.random.Random

class RandomCarMoveService : CarMoveService {

    override fun move(car: Car) {
        val randomValue = Random.nextInt(0, 10)  // 0부터 9까지의 임의의 정수 생성
        car.move(randomValue)
    }
}