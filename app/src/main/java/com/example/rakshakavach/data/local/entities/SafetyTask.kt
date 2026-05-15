package com.example.rakshakavach.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "safety_tasks")
data class SafetyTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskName: String,
    val requiredEquipment: String // Comma separated items
)
