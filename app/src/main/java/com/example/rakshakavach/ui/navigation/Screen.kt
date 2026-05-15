package com.example.rakshakavach.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object TaskSelection : Screen("task_selection")
    object SafetyChecklist : Screen("safety_checklist/{taskName}") {
        fun createRoute(taskName: String) = "safety_checklist/$taskName"
    }
    object RiskMeter : Screen("risk_meter/{missingItems}") {
        fun createRoute(missingItems: String) = "risk_meter/$missingItems"
    }
    object VisualSafety : Screen("visual_safety")
    object IncidentReport : Screen("incident_report")
    object SafetyQuiz : Screen("safety_quiz")
    object SafetyScore : Screen("safety_score")
    object Profile : Screen("profile")
}
