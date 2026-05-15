package com.example.rakshakavach.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quizzes")
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val options: String, // Comma separated options
    val correctAnswer: String,
    val score: Int
)
