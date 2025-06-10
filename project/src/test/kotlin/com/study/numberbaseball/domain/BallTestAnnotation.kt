package com.study.numberbaseball.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions


class BallTestAnnotation {

    @Test
    fun strike() {

        val ball = Ball(3, 1)
        val comparedBall = Ball(3, 1)

        val result = ball.play(comparedBall)

        Assertions.assertEquals(result, BallStatus.STRIKE)
    }

    @Test
    fun ball() {

        val ball = Ball(3, 2)
        val comparedBall = Ball(3, 1)


        val result = ball.play(comparedBall)

        Assertions.assertEquals(result, BallStatus.BALL)
    }

    @Test
    fun out() {

        val ball = Ball(3, 2)
        val comparedBall = Ball(1, 1)

        val result = ball.play(comparedBall)

        Assertions.assertEquals(result, BallStatus.OUT)
    }
}