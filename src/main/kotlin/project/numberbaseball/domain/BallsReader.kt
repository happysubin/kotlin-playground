package project.numberbaseball.domain

import project.numberbaseball.domain.Balls

interface BallsReader {
    fun read(): Balls
}