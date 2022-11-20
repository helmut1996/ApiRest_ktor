package com.example.models.notes

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(val note:String,val idfolder:Int)
