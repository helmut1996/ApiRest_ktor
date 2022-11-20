package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.autentication.User
import io.ktor.server.config.*

class TokenManager(val config:HoconApplicationConfig) {
    fun generateJWTToken(user:User):String{
    val audience = config.property("audience").getString()
    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val expirationData = System.currentTimeMillis()+6000;

        val tohen = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username",user.username)
            .withClaim("userId",user.id)
            .sign(Algorithm.HMAC256(secret))
        return tohen
    }
}