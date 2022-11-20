package com.example.models.autentication

import kotlinx.serialization.Serializable

@Serializable
data class User(val id:Int, val username:String,val password:String)
