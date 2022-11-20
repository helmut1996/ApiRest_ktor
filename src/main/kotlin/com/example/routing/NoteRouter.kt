package com.example.routing

import com.example.db.DatabaseConnection
import com.example.entities.NotesEntitty
import com.example.models.notes.NoteRequest
import com.example.models.notes.NoteResponse
import com.example.models.notes.Notes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.liuwj.ktorm.dsl.*

fun Application.noteRouting(){
    val db = DatabaseConnection.database
    routing {
        get ("/notes"){
            val note = db.from(NotesEntitty).select()
                .map {
                    val id = it[NotesEntitty.id]
                    val notes = it[NotesEntitty.note]
                    val folder = it[NotesEntitty.id_folder]
                    Notes(id ?:-1 , notes ?:"",folder ?:-1)
                }
            call.respond(note)
           // call.respondText("Aqui se va mostrar JSON de las notas")
        }
        post("/note"){
            val request = call.receive<NoteRequest>()
            val result = db.insert(NotesEntitty){
                set(it.note,request.note)
                set(it.id_folder, request.idfolder)
            }


            if (result == 1){
                //Send successfully response to the clientSend successfully response to the client call.respond()
                call.respond(HttpStatusCode.OK, NoteResponse(
                    success = true,
                    date = "Values success "
                )
                )
            }else{
                call.respond(HttpStatusCode.BadRequest, NoteResponse(
                    success = false,
                    date = "Values error insert "
                )
                )
            }


        }
        get("/notes/{id}"){
            val id = call.parameters["id"]?.toInt() ?: -1

            val note = db.from(NotesEntitty)
                .select()
                .where { NotesEntitty.id eq id }
                .map {
                    val id = it[NotesEntitty.id]!!
                    val note = it[NotesEntitty.note]!!
                    val folder = it[NotesEntitty.id_folder]!!
                    Notes(id = id, note = note, idfolder = folder)
                }.firstOrNull()

                 if (note ==null){
                     call.respond(
                         HttpStatusCode.NotFound, NoteResponse(
                             success = false,
                             date = "Register not found id = ${id}"
                         )
                     )
                 }else{
                    call.respond(
                        HttpStatusCode.OK, NoteResponse(
                            success = true,
                            date = note
                        )
                    )
                 }
        }
        put("/notes/{id}"){
            val updateNote = call.receive<NoteRequest>()
            val id = call.parameters["id"]?.toInt() ?: -1
            val RowEffect = db.update(NotesEntitty){
                set(it.note, updateNote.note)
                set(it.id_folder, updateNote.idfolder)
                where {
                    it.id eq id
                }
            }

            if (RowEffect== 1){
            call.respond(
                HttpStatusCode.OK,
                NoteResponse(
                    success = true,
                    date = "Update note success"
                )
            )
            }else{
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        date = "Update note error"
                    )
                )
            }

        }
        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val rowEffected = db.delete(NotesEntitty){
                it.id eq id
            }

            if (rowEffected== 1){
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        success = true,
                        date = "Delete note success"
                    )
                )
            }else{
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        date = "Delete note error"
                    )
                )
            }
        }
    }
}