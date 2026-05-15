package com.example.rakshakavach.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rakshakavach.data.local.dao.IncidentDao
import com.example.rakshakavach.data.local.dao.QuizDao
import com.example.rakshakavach.data.local.dao.SafetyTaskDao
import com.example.rakshakavach.data.local.dao.UserDao
import com.example.rakshakavach.data.local.entities.Incident
import com.example.rakshakavach.data.local.entities.Quiz
import com.example.rakshakavach.data.local.entities.SafetyTask
import com.example.rakshakavach.data.local.entities.User

@Database(
    entities = [User::class, Incident::class, Quiz::class, SafetyTask::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun incidentDao(): IncidentDao
    abstract fun quizDao(): QuizDao
    abstract fun safetyTaskDao(): SafetyTaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "raksha_kavach_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
