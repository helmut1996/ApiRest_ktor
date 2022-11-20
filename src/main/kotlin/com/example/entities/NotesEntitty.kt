package com.example.entities

import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar


object NotesEntitty: Table<Nothing>("note"){

    val id = int("id").primaryKey()
    val note =  varchar("note")
    val id_folder = int("id_folder")


}