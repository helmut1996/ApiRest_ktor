package com.example.models.notes

import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse<T>(
    val date:T,
    val success:Boolean
)
