package project.racingcar.infra

import project.racingcar.domain.Car
import project.racingcar.domain.Cars
import project.racingcar.domain.Position
import project.racingcar.presentation.CarsReader
import project.racingcar.presentation.GameCountReader
import java.util.*

class ConsoleReader : CarsReader, GameCountReader {

    private val scanner: Scanner = Scanner(System.`in`)

    override fun readCars(): Cars {
        //TODO 예외 처리 추가해야함.
        println("경주할 자동차 이름을 입력하세요(이름은 쉼표(,)를 기준으로 구분).")
        val str = scanner.nextLine()
        val names = str.split(",")
        return Cars(names.map { Car(it, Position(1)) })
    }

    override fun readCount(): Int {
        println("시도할 횟수는 몇회인가요?")
        val nums = scanner.nextLine()
        if(isNotNumeric(nums)) throw IllegalArgumentException("숫자만 입력해주세요.")
        return nums.toInt()
    }

    private fun isNotNumeric(input: String): Boolean {
        return input.toIntOrNull() == null
    }
}