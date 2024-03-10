package project.racingcar

data class Position(private val position: Int) {

    fun move(moveValue: Int): Position {
        return Position(position + moveValue)
    }
}