package project.numberbaseball.domain

class BaseBallGame(
    private val playerBalls: Balls,
    private val gameBalls: Balls
) {
    fun play(): Result {
        val results = gameBalls.play(playerBalls)
        return Result.obtain(results)
    }
}