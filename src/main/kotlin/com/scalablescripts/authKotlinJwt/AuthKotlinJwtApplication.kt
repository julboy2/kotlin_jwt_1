package com.scalablescripts.authKotlinJwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class AuthKotlinJwtApplication

fun main(args: Array<String>) {
	runApplication<AuthKotlinJwtApplication>(*args)
}
