package project.racingcar

class Cars(
    private val _cars: List<Car>
) {

    fun move(carMoveService: CarMoveService) {
        _cars.forEach{ carMoveService.move(it) }
    }

    val cars: List<Car>
        get() = _cars
}