package com.scalablescripts.authKotlinJwt.repositories

import com.scalablescripts.authKotlinJwt.models.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int> {

    fun findByEmail(email:String): User?
}