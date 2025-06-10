package com.study.numberbaseball.domain

class Ball(
    private val num: Int,
    private val position: Int
) {

    fun play(ball: Ball): BallStatus {
        if(ball.strike(num, position)) {
            return BallStatus.STRIKE
        }

        else if(ball.ball(num)) {
            return BallStatus.BALL
        }

        return BallStatus.OUT
    }

    private fun strike(num: Int, position: Int): Boolean {
        return (this.num == num) && (this.position == position)
    }

    private fun ball(num: Int): Boolean {
        return this.num == num
    }

    override fun toString(): String {
        return "Ball(num=$num, position=$position)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ball

        if (num != other.num) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = num
        result = 31 * result + position
        return result
    }


}