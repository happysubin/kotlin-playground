package com.study.numberbaseball.domain

import com.study.numberbaseball.domain.Balls

interface BallsReader {
    fun read(): Balls
}