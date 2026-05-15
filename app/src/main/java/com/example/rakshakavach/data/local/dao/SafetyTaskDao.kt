package com.example.rakshakavach.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rakshakavach.data.local.entities.SafetyTask
import kotlinx.coroutines.flow.Flow

@Dao
interface SafetyTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<SafetyTask>)

    @Query("SELECT * FROM safety_tasks")
    fun getAllTasks(): Flow<List<SafetyTask>>

    @Query("SELECT * FROM safety_tasks WHERE taskName = :name LIMIT 1")
    suspend fun getTaskByName(name: String): SafetyTask?
}
