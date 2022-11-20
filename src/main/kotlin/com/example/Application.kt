package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.plugins.*
import com.example.routing.AutotaticationRouting
import com.example.routing.noteRouting
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.auth.jwt.*
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.*
import me.liuwj.ktorm.dsl.*

val config = HoconApplicationConfig(ConfigFactory.load())
val tokenManager = TokenManager(config)
fun main() {

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)

        .start(wait = true)


}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }
    authentication {
        jwt {
            verifier(tokenManager.verifyJWTToken())
            realm = config.property("realm").getString()
            validate {jwtCredential ->
                if (jwtCredential.payload.getClaim("username").asString().isNotEmpty()){
                    io.ktor.server.auth.jwt.JWTPrincipal(jwtCredential.payload)
                }else{
                    null
                }

            }
        }
    }
    configureRouting()




    //insert()
    // update()
    //delete()
    // read()

}
/*
fun insert(){
    database.insert(NotesEntitty){
        set(it.note,"Inser5tando en ktor")
        set(it.id_folder,3)
    }
}

fun read(){
    val notes = database.from(NotesEntitty).select()

    for (row in notes){
        println("${row[NotesEntitty.id]}:${row[NotesEntitty.note]} : ${row[NotesEntitty.id_folder]}")
    }
}

fun update(){
    database.update(NotesEntitty){
        set(it.note, "helmut brenes")
        where {
            it.id eq 1
        }
    }
}

fun delete(){
    database.delete(NotesEntitty){
        it.id eq 3
    }
 */
