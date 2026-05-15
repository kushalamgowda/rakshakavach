package com.example.rakshakavach.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rakshakavach.data.local.entities.Quiz
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): Flow<List<Quiz>>
}
