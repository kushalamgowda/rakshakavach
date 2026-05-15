package com.example.rakshakavach.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rakshakavach.data.local.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE phone = :phone AND password = :password LIMIT 1")
    suspend fun login(phone: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Flow<User>

    @Update
    suspend fun updateUser(user: User)

    @Query("UPDATE users SET totalQuizScore = totalQuizScore + :points, completedQuizzes = completedQuizzes + 1 WHERE id = :userId")
    suspend fun updateQuizScore(userId: Int, points: Int)
    
    @Query("UPDATE users SET quizStreak = quizStreak + 1 WHERE id = :userId")
    suspend fun incrementQuizStreak(userId: Int)
}
