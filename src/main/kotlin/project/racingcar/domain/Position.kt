package project.racingcar.domain

data class Position(val value: Int) {

    fun move(moveValue: Int): Position {
        return Position(value + moveValue)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for(i in 1..value) {
            builder.append("=")
        }
        return builder.toString()
    }
}