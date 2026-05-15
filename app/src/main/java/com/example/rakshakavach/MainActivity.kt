package com.example.rakshakavach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rakshakavach.data.local.AppDatabase
import com.example.rakshakavach.data.remote.GeminiService
import com.example.rakshakavach.data.repository.MainRepository
import com.example.rakshakavach.ui.navigation.Screen
import com.example.rakshakavach.ui.screens.*
import com.example.rakshakavach.ui.theme.RakshakavachTheme
import com.example.rakshakavach.ui.viewmodel.MainViewModel
import com.example.rakshakavach.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(applicationContext)
        val apiKey = BuildConfig.GEMINI_API_KEY
        
        val geminiService = GeminiService(apiKey = apiKey)
        val repository = MainRepository(
            database.userDao(),
            database.incidentDao(),
            database.quizDao(),
            database.safetyTaskDao(),
            geminiService
        )
        val viewModelFactory = ViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            RakshakavachTheme {
                MainNavigation(viewModelFactory, repository)
            }
        }
    }
}

@Composable
fun MainNavigation(factory: ViewModelFactory, repository: MainRepository) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = mainViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                repository = repository
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route)
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
                repository = repository
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = mainViewModel,
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable(Screen.TaskSelection.route) {
            TaskSelectionScreen(
                viewModel = mainViewModel,
                onTaskSelected = { taskName ->
                    navController.navigate(Screen.SafetyChecklist.createRoute(taskName))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.SafetyChecklist.route,
            arguments = listOf(navArgument("taskName") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskName = backStackEntry.arguments?.getString("taskName") ?: ""
            SafetyChecklistScreen(
                viewModel = mainViewModel,
                taskName = taskName,
                onComplete = {
                    navController.navigate(Screen.VisualSafety.route)
                },
                onRiskDetected = { missingItems ->
                    navController.navigate(Screen.RiskMeter.createRoute(missingItems))
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.VisualSafety.route) {
            VisualSafetyScreen(
                onContinue = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.RiskMeter.route,
            arguments = listOf(navArgument("missingItems") { type = NavType.StringType })
        ) { backStackEntry ->
            val missingItems = backStackEntry.arguments?.getString("missingItems") ?: ""
            RiskMeterScreen(
                viewModel = mainViewModel,
                missingItems = missingItems,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.IncidentReport.route) {
            IncidentReportingScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SafetyQuiz.route) {
            QuizScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = mainViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.SafetyScore.route) {
            SafetyScoreScreen(
                viewModel = mainViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
