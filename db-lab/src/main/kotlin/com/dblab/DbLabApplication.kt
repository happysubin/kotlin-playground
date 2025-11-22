package com.dblab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DbLabApplication

fun main(args: Array<String>) {
    runApplication<DbLabApplication>(*args)
}
