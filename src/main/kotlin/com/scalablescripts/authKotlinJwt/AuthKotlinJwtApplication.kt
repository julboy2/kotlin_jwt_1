package com.scalablescripts.authKotlinJwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthKotlinJwtApplication

fun main(args: Array<String>) {
	runApplication<AuthKotlinJwtApplication>(*args)
}
