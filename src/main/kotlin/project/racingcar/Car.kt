package project.racingcar

class Car(
    private val _name: String,
    private var _position: Position
) {
    fun move(moveValue: Int) {
        _position = _position.move(moveValue)
    }

    val name: String
        get() = _name
    val position: Position
        get() = _position

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Car) return false

        if (_name != other._name) return false
        if (_position != other._position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _name.hashCode()
        result = 31 * result + _position.hashCode()
        return result
    }
}