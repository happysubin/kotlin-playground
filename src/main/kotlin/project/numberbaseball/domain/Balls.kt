package project.numberbaseball.domain

class Balls (
    private val balls : List<Ball>
) {

    fun play(comparedBalls: Balls): List<BallStatus> {
        return balls.map { ball -> comparedBalls.play(ball) }
    }

    //Elvis 연산자를 사용하여 null이 반환될 경우 기본값으로 BallStatus.OUT을 반환
    fun play(attemptedBall: Ball): BallStatus {
        return balls
            .map { ball -> ball.play(attemptedBall) }
            .firstOrNull { ballStatus -> ballStatus != BallStatus.OUT }
            ?: BallStatus.OUT

    }
}