package com.example.rakshakavach.data.remote

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService(apiKey: String) {
    private val model = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    suspend fun generateSafetyChecklist(task: String): String? = withContext(Dispatchers.IO) {
        try {
            val response = model.generateContent(content {
                text("Generate a list of mandatory Personal Protective Equipment (PPE) for the construction task: $task. Format the output as a comma-separated list of items.")
            })
            response.text
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRiskExplanation(missingItems: String): String? = withContext(Dispatchers.IO) {
        try {
            val response = model.generateContent(content {
                text("Explain the risks and potential injuries if a worker performs a task without the following safety gear: $missingItems. Be concise and impactful.")
            })
            response.text
        } catch (e: Exception) {
            null
        }
    }

    suspend fun generateSafetyQuiz(): String? = withContext(Dispatchers.IO) {
        try {
            val response = model.generateContent(content {
                text("Generate a multiple-choice safety quiz question for a construction worker. Format: Question | Option1, Option2, Option3, Option4 | CorrectOption")
            })
            response.text
        } catch (e: Exception) {
            null
        }
    }
}
