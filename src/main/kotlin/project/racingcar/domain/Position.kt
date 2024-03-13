package project.racingcar.domain

data class Position(private val position: Int) {

    fun move(moveValue: Int): Position {
        return Position(position + moveValue)
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for(i in 1..position) {
            builder.append("=")
        }
        return builder.toString()
    }
}