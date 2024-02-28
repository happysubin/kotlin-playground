package project.numberbaseball.domain

class Result(
    val strike: Int = 0,
    val ball: Int = 0
) {

    companion object {
        fun obtain(statusList: List<BallStatus>): Result  {
            var result = Result()
            for (status in statusList) {
                if(status == BallStatus.STRIKE) {
                    result = Result(strike = result.strike + 1, ball = result.ball)
                }
                else if(status == BallStatus.BALL) {
                    result = Result(strike = result.strike, ball = result.ball + 1)
                }
            }
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (strike != other.strike) return false
        if (ball != other.ball) return false

        return true
    }

    override fun hashCode(): Int {
        var result = strike
        result = 31 * result + ball
        return result
    }

    override fun toString(): String {
        return "Result(strike=$strike, ball=$ball)"
    }


}

