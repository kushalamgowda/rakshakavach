package com.example.rakshakavach.data.repository

import com.example.rakshakavach.data.local.dao.IncidentDao
import com.example.rakshakavach.data.local.dao.QuizDao
import com.example.rakshakavach.data.local.dao.SafetyTaskDao
import com.example.rakshakavach.data.local.dao.UserDao
import com.example.rakshakavach.data.local.entities.Incident
import com.example.rakshakavach.data.local.entities.Quiz
import com.example.rakshakavach.data.local.entities.SafetyTask
import com.example.rakshakavach.data.local.entities.User
import com.example.rakshakavach.data.remote.GeminiService
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val userDao: UserDao,
    private val incidentDao: IncidentDao,
    private val quizDao: QuizDao,
    private val safetyTaskDao: SafetyTaskDao,
    private val geminiService: GeminiService
) {
    // User
    suspend fun registerUser(user: User) = userDao.insertUser(user)
    suspend fun login(phone: String, password: String) = userDao.login(phone, password)
    fun getUserById(id: Int): Flow<User> = userDao.getUserById(id)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun updateQuizScore(userId: Int, points: Int) = userDao.updateQuizScore(userId, points)
    suspend fun incrementQuizStreak(userId: Int) = userDao.incrementQuizStreak(userId)

    // Incidents
    suspend fun reportIncident(incident: Incident) = incidentDao.insertIncident(incident)
    fun getAllIncidents(): Flow<List<Incident>> = incidentDao.getAllIncidents()
    fun getIncidentsByUserId(userId: Int): Flow<List<Incident>> = incidentDao.getIncidentsByUserId(userId)

    // Quizzes
    suspend fun saveQuiz(quiz: Quiz) = quizDao.insertQuiz(quiz)
    fun getAllQuizzes(): Flow<List<Quiz>> = quizDao.getAllQuizzes()

    // Tasks
    suspend fun insertTasks(tasks: List<SafetyTask>) = safetyTaskDao.insertTasks(tasks)
    fun getAllTasks(): Flow<List<SafetyTask>> = safetyTaskDao.getAllTasks()
    suspend fun getTaskByName(name: String) = safetyTaskDao.getTaskByName(name)

    // AI
    suspend fun getAIChecklist(task: String) = geminiService.generateSafetyChecklist(task)
    suspend fun getAIRiskExplanation(missingItems: String) = geminiService.getRiskExplanation(missingItems)
    suspend fun getAIQuiz() = geminiService.generateSafetyQuiz()
}
