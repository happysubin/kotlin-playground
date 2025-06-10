package com.study.numberbaseball.infra

import com.study.numberbaseball.domain.Ball
import com.study.numberbaseball.domain.Balls
import com.study.numberbaseball.domain.RandomBallsFactory

class RandomBallsFactoryImpl: RandomBallsFactory {
    override fun create(): Balls {
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val shuffled = list.shuffled()
        return Balls(listOf(Ball(shuffled.get(0), 1), Ball(shuffled.get(1), 2), Ball(shuffled.get(2), 3)))
    }
}