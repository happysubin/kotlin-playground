package com.study.numberbaseball.infra

import com.study.numberbaseball.domain.Result
import com.study.numberbaseball.domain.ResultView

class ResultViewImpl: ResultView {
    override fun print(result: Result) {
        println(result)
    }
}