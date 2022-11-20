package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.UserEntity
import com.example.models.autentication.User
import com.example.models.autentication.UserCredentials
import com.example.models.notes.NoteResponse
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.liuwj.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.AutotaticationRouting(){
val db = DatabaseConnection.database
val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    routing {
        post ( "/register"){
            val userCredentials = call.receive<UserCredentials>()

            val username = userCredentials.user.toLowerCase()
            val password = userCredentials.hashedPassword()


            if(!userCredentials.isValidCredentials()){
                call.respond(HttpStatusCode.BadRequest,NoteResponse(
                    success = false,
                    date = "username should be greater tha or equal to 3 and password should be greater tha or equal to 8"
                    ))
                return@post
            }


            //check if username already exists
            val user = db.from(UserEntity)
                .select()
                .where {
                    UserEntity.username eq  username }
                .map {
                    it[UserEntity.username] }
                .firstOrNull()

            if (user != null){
                call.respond(HttpStatusCode.BadRequest,NoteResponse(
                    success = false,
                    date = "User alredy exist, please try different username"))
                    return@post
            }


            db.insert(UserEntity){

                set(it.username,username)
                set(it.password,password)

            }
            call.respondText("new user Insert")
        }



        post("/login") {
            val userCredentialss = call.receive<UserCredentials>()
            val username = userCredentialss.user.toLowerCase()
            val password = userCredentialss.password

            if(!userCredentialss.isValidCredentials()){
                call.respond(HttpStatusCode.BadRequest,NoteResponse(
                    success = false,
                    date = "username should be greater tha or equal to 3 and password should be greater tha or equal to 8"
                ))
                return@post
            }
            //check user exists
            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.username eq username }
                .map {
                    val id = it[UserEntity.id]!!
                    val user = it[UserEntity.username]!!
                    val pass = it[UserEntity.password]!!
                    User(id = id,username=user, password = pass)
                }.firstOrNull()

            if (user == null){
                call.respond(HttpStatusCode.BadRequest,NoteResponse(
                    success = false,
                    date = "invalid username or password"))
                return@post
            }

            val doesPasswordMatch = BCrypt.checkpw(password,user?.password)
            if (!doesPasswordMatch){
                call.respond(HttpStatusCode.BadRequest,
                NoteResponse(
                    success = false,
                    date = "Invalid Usename or password"))
                return@post
            }

            //generate token
            val token = tokenManager.generateJWTToken(user)
            call.respond(HttpStatusCode.OK,
            NoteResponse(success = true, date = token))

        }

    authenticate{
        get("/me") {
            val principle= call.principal<JWTPrincipal>()
            val username = principle!!.payload.getClaim("username").asString()
            val userId = principle!!.payload.getClaim("userId").asInt()
            call.respondText("Hola $username su ID: $userId")
        }
    }
    }
}