package com.example

import com.example.entities.NotesEntitty
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.routing.AutotaticationRouting
import com.example.routing.noteRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.util.*
import me.liuwj.ktorm.database.Database
import me.liuwj.ktorm.dsl.*
import javax.naming.AuthenticationException

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)

        .start(wait = true)



}

fun Application.module() {

    configureRouting()
    noteRouting()
    AutotaticationRouting()
    install(ContentNegotiation) {
        json()
    }

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
