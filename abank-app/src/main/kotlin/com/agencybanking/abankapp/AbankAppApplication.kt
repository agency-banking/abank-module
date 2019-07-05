package com.agencybanking.abankapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AbankAppApplication

fun main(args: Array<String>) {
	runApplication<AbankAppApplication>(*args)
}
