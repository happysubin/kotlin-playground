package project.racingcar.domain

class Winner {

    companion object {
        fun extract(cars: List<Car>): String {
            val maxPosition = cars.maxOfOrNull { it.position.value }
            val winners = cars.filter { it.position.value == maxPosition }
            return if (winners.isNotEmpty()) winners.joinToString { it.name } else "No cars"
        }
    }
}