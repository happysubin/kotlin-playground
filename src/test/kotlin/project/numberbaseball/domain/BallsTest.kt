package project.numberbaseball.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BallsTest {

    @Test
    @DisplayName("3스트라이크 0볼")
    fun test1() {
        val userBalls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(5, 3)))
        val computerBalls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(5, 3)))

        val result = userBalls.play(computerBalls)

        assertEquals(result, Result(3, 0))
    }

    @Test
    @DisplayName("2스트라이크 0볼")
    fun test2() {
        val userBalls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(7, 3)))
        val computerBalls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(5, 3)))

        val result = userBalls.play(computerBalls)

        assertEquals(result, Result(2, 0))
    }

    @Test
    @DisplayName("1스트라이크 0볼")
    fun test3() {
        val userBalls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(7, 3)))
        val computerBalls = Balls(listOf(Ball(3, 1), Ball(5, 2), Ball(6, 3)))

        val result = userBalls.play(computerBalls)

        assertEquals(result, Result(1, 0))
    }

    @Test
    @DisplayName("0스트라이크 0볼")
    fun test4() {
        val userBalls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(7, 3)))
        val computerBalls = Balls(listOf(Ball(9, 1), Ball(8, 2), Ball(6, 3)))

        val result = userBalls.play(computerBalls)

        assertEquals(result, Result(0, 0))
    }

    @Test
    @DisplayName("0스트라이크 1볼")
    fun test5() {
        val userBalls = Balls(listOf(Ball(4, 1), Ball(9, 2), Ball(7, 3)))
        val computerBalls = Balls(listOf(Ball(9, 1), Ball(8, 2), Ball(6, 3)))

        val result = userBalls.play(computerBalls)

        println(result)
        assertEquals(result, Result(0, 1))
    }

    @Test
    @DisplayName("0스트라이크 2볼")
    fun test6() {
        val userBalls = Balls(listOf(Ball(4, 1), Ball(9, 2), Ball(7, 3)))
        val computerBalls = Balls(listOf(Ball(7, 1), Ball(4, 2), Ball(1, 3)))

        val result = userBalls.play(computerBalls)

        println(result)
        assertEquals(result, Result(0, 2))
    }

    @Test
    @DisplayName("0스트라이크 3볼")
    fun test7() {
        val userBalls = Balls(listOf(Ball(4, 1), Ball(9, 2), Ball(7, 3)))
        val computerBalls = Balls(listOf(Ball(7, 1), Ball(4, 2), Ball(9, 3)))

        val result = userBalls.play(computerBalls)

        println(result)
        assertEquals(result, Result(0, 3))
    }

}