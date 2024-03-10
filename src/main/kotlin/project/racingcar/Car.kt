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
}