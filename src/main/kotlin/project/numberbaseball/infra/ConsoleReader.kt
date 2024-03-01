package project.numberbaseball.infra

import project.numberbaseball.domain.Ball
import project.numberbaseball.domain.Balls
import project.numberbaseball.domain.BallsReader
import project.numberbaseball.domain.UserInputReader
import java.util.*

class ConsoleReader: BallsReader, UserInputReader {

    private val scanner: Scanner = Scanner(System.`in`)

    override fun read(): Balls {
        println("숫자를 입력해주세요")
        val nums: String = scanner.nextLine()
        validate(nums)
        val balls = createBalls(nums)
        return Balls(balls)
    }

    private fun validate(nums: String) {
        if(!nums.all{ it.isDigit() }) {
            throw IllegalArgumentException("숫자만 입력해주세요.")
        }

        if(nums.toCharArray().size != 3) {
            throw IllegalArgumentException("3개의 숫자만 입력해주세요.")
        }
    }

    private fun createBalls(nums: String): List<Ball> {
        return nums
            .toCharArray()
            .withIndex()
            .map { (index, c) ->
                Ball(position = index + 1, num = c.toString().toInt())
            }
    }

    override fun getGameRestartFlag(): Boolean {
        println("게임을 새로 시작하려면 1, 종료하려면 2를 입력하세요.")
        val flag = scanner.nextInt()
        if(flag == 1) return true
        else if(flag == 2) return false
        else throw IllegalArgumentException("올바른 값이 아닙니다")
    }
}