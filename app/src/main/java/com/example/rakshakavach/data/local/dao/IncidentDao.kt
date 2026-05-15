package com.example.rakshakavach.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rakshakavach.data.local.entities.Incident
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidentDao {
    @Insert
    suspend fun insertIncident(incident: Incident)

    @Query("SELECT * FROM incidents ORDER BY date DESC")
    fun getAllIncidents(): Flow<List<Incident>>

    @Query("SELECT * FROM incidents WHERE userId = :userId ORDER BY date DESC")
    fun getIncidentsByUserId(userId: Int): Flow<List<Incident>>
}
