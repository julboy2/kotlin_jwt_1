package com.scalablescripts.authKotlinJwt

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc   // 웹mvc 활성화
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            // 프론트엔드 url 여러개면 , 로 표시
            .allowedOrigins("http://localhost:3000")
            // 자격증명 true 로  허용해야 jwt쿠키를 추가할수 있다.
            .allowCredentials(true)
    }
}