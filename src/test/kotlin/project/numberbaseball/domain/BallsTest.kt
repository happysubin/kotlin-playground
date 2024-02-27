package project.numberbaseball.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BallsTest {

    @Test
    fun strike() {
        val balls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(5, 3)))

        val result = balls.play(Ball(3, 1))

        assertEquals(result, BallStatus.STRIKE)
    }

    @Test
    fun ball() {
        val balls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(5, 3)))

        val result = balls.play(Ball(4, 1))

        assertEquals(result, BallStatus.BALL)
    }

    @Test
    fun out() {
        val balls = Balls(listOf(Ball(3, 1), Ball(4, 2), Ball(5, 3)))

        val result = balls.play(Ball(9, 1))

        assertEquals(result, BallStatus.OUT)
    }

}