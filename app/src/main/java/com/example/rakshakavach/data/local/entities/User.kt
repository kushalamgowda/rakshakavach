package com.example.rakshakavach.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val employeeId: String = "",
    val phone: String,
    val password: String,
    val jobRole: String = "",
    val companyName: String = "",
    val profilePictureUri: String? = null,
    val bio: String? = null,
    val totalQuizScore: Int = 0,
    val quizStreak: Int = 0,
    val completedQuizzes: Int = 0,
    val trainingBadges: String = "", // Comma-separated list of badge names
    val supervisorPhone: String = "",
    val familyPhone: String = "",
    val languagePreference: String = "English",
    val safetyScorePercentage: Int = 0,
    val incidentFreeDays: Int = 0
)
