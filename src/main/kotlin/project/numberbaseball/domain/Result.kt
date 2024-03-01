package project.numberbaseball.domain

data class Result(
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

        fun isContinue(result: Result ): Boolean {
            if(result == Result(3, 0)) {
                println("3개의 숫자를 모두 맞히셨습니다! 게임 종료")
                return false;
            }
            return true;
        }
    }

    override fun toString(): String {
        if(ball == 0 && strike == 0) return "아웃"
        return "$ball 볼 $strike 스트라이크"
    }
}

