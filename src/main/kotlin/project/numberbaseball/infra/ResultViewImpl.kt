package project.numberbaseball.infra

import project.numberbaseball.domain.Result
import project.numberbaseball.domain.ResultView

class ResultViewImpl: ResultView {
    override fun print(result: Result) {
        println(result)
    }
}