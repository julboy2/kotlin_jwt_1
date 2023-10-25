package com.scalablescripts.authKotlinJwt.controllers

import com.scalablescripts.authKotlinJwt.dtos.LoginDTO
import com.scalablescripts.authKotlinJwt.dtos.Message
import com.scalablescripts.authKotlinJwt.dtos.RegisterDTO
import com.scalablescripts.authKotlinJwt.models.User
import com.scalablescripts.authKotlinJwt.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.util.*

@RestController
@RequestMapping("api")
class AuthController(
    private val userService: UserService
) {
    val secretKey = "abcde"
    val secretKeyEncode = Base64.getEncoder().encodeToString(secretKey.toByteArray())

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<User>{
        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password
        return ResponseEntity.ok(this.userService.save(user))
    }


    @PostMapping("login")
    fun login(
        @RequestBody body: LoginDTO ,
        response: HttpServletResponse ,
    ): ResponseEntity<Any>{
        val user= this.userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found!"))

        if(!user.comparePassword(body.password)){
            return ResponseEntity.badRequest().body(Message("invalid password!"))
        }

        /**
         * jwt 시작
         */

        val issuer = user.id.toString()
        // JWT를 생성하고 검증하는 컴포넌트

        val jwt = Jwts.builder()
            .setIssuer(issuer)    // 발행자
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 만료일 1 day
        //암호 알고리즘 , 암호화키
            .signWith(SignatureAlgorithm.HS256,secretKeyEncode)
        // jwt 문자열 형태로 직렬화하는 역할 : 이세 부분을 합쳐서 하나의 문자열로 만들어줌
        // 헤더 , 페이로드 , 서명이 합쳐진 완전한 JWT 문자열로 만들어줌
            .compact()

        // 쿠키생성
        val cookie = Cookie("jwt",jwt)
        cookie.isHttpOnly = true
        response.addCookie(cookie)  // 프론트엔드에서 받게된다.

        return ResponseEntity.ok(Message("success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any>{
        try {
            if (jwt == null){
                return ResponseEntity.status(401).body(Message("unauthenticated"))
            }

            val body = Jwts.parser().setSigningKey(secretKeyEncode).parseClaimsJws(jwt).body
            return ResponseEntity.ok(this.userService.getById(body.issuer.toInt()))
        }catch (e: Exception){
            return ResponseEntity.status(401).body(Message("unauthenticated"))
        }
    }


    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any>{
        val cookie = Cookie("jwt","")
        cookie.maxAge = 0

        response.addCookie(cookie)
        return ResponseEntity.ok(Message("success"))
    }


}