package com.example.models.notes

import kotlinx.serialization.Serializable

@Serializable
data class Notes(val id:Int, val note:String, val idfolder:Int)
