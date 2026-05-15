package com.example.rakshakavach.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rakshakavach.data.local.entities.Incident
import com.example.rakshakavach.data.local.entities.Quiz
import com.example.rakshakavach.data.local.entities.SafetyTask
import com.example.rakshakavach.data.local.entities.User
import com.example.rakshakavach.data.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _tasks = MutableStateFlow<List<SafetyTask>>(emptyList())
    val tasks: StateFlow<List<SafetyTask>> = _tasks.asStateFlow()

    private val _checklist = MutableStateFlow<List<String>>(emptyList())
    val checklist: StateFlow<List<String>> = _checklist.asStateFlow()

    private val _riskExplanation = MutableStateFlow<String?>(null)
    val riskExplanation: StateFlow<String?> = _riskExplanation.asStateFlow()

    val userIncidents: StateFlow<List<Incident>> = _currentUser.flatMapLatest { user ->
        user?.let { repository.getIncidentsByUserId(it.id) } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect {
                if (it.isEmpty()) {
                    // Seed initial data
                    val initialTasks = listOf(
                        SafetyTask(taskName = "Welding", requiredEquipment = "Helmet,Gloves,Goggles,Apron,Safety Shoes"),
                        SafetyTask(taskName = "Height Work", requiredEquipment = "Harness,Helmet,Safety Shoes,Lanyard"),
                        SafetyTask(taskName = "Electrical Repair", requiredEquipment = "Insulated Gloves,Voltage Tester,Safety Shoes,Goggles"),
                        SafetyTask(taskName = "Digging Trench", requiredEquipment = "Helmet,High-Vis Vest,Safety Shoes,Gloves"),
                        SafetyTask(taskName = "Machine Operation", requiredEquipment = "Goggles,Ear Protection,Safety Shoes,Gloves")
                    )
                    repository.insertTasks(initialTasks)
                }
                _tasks.value = it
            }
        }
    }

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun updateProfile(updatedUser: User) {
        viewModelScope.launch {
            repository.updateUser(updatedUser)
            _currentUser.value = updatedUser
        }
    }

    fun addQuizScore(points: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.updateQuizScore(user.id, points)
            repository.incrementQuizStreak(user.id)
            // Refresh user data (In a real app, Room Flow would handle this automatically)
            repository.getUserById(user.id).collect {
                _currentUser.value = it
            }
        }
    }

    fun getChecklist(taskName: String) {
        viewModelScope.launch {
            // Try AI first, then fallback to local
            val aiChecklist = repository.getAIChecklist(taskName)
            if (aiChecklist != null) {
                _checklist.value = aiChecklist.split(",").map { it.trim() }
            } else {
                val localTask = repository.getTaskByName(taskName)
                _checklist.value = localTask?.requiredEquipment?.split(",")?.map { it.trim() } ?: emptyList()
            }
        }
    }

    fun getRiskExplanation(missingItems: List<String>) {
        viewModelScope.launch {
            val explanation = repository.getAIRiskExplanation(missingItems.joinToString(", "))
            _riskExplanation.value = explanation ?: "Working without proper safety gear significantly increases the risk of severe injuries. Please wear all mandatory equipment."
        }
    }

    fun reportIncident(type: String, description: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val incident = Incident(
                userId = user.id,
                incidentType = type,
                description = description,
                date = System.currentTimeMillis()
            )
            repository.reportIncident(incident)
        }
    }
}
