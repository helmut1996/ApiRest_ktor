package com.example.models.autentication

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserCredentials( val user:String, val password:String){
    fun hashedPassword():String{
        return BCrypt.hashpw(password,BCrypt.gensalt())
    }
    fun isValidCredentials():Boolean{
        return user.length>= 3 && password.length>=6
    }
}