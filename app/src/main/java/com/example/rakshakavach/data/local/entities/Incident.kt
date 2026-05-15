package com.example.rakshakavach.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidents")
data class Incident(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val incidentType: String,
    val description: String,
    val date: Long,
    val imageUri: String? = null
)
