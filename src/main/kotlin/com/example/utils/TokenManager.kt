package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.autentication.User
import io.ktor.server.config.*
import java.util.Date

class TokenManager(val config:HoconApplicationConfig) {
    val audience = config.property("audience").getString()
    val secret = config.property("secret").getString()
    val issuer = config.property("issuer").getString()
    val expirationData = System.currentTimeMillis()+6000;
    fun generateJWTToken(user:User):String{

        val tohen = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username",user.username)
            .withClaim("userId",user.id)
            .withExpiresAt(Date(expirationData))
            .sign(Algorithm.HMAC256(secret))
        return tohen
    }

    fun verifyJWTToken():JWTVerifier{
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}