package project.numberbaseball.domain

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

}