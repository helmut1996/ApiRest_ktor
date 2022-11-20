package com.example.db

import me.liuwj.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/bd_notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = ""
    )
}