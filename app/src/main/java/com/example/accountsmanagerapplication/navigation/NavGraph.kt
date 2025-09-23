package com.example.accountsmanagerapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.accountsmanagerapplication.ui.CreateProjectScreen
import com.example.accountsmanagerapplication.ui.DashboardScreen
import com.example.accountsmanagerapplication.ui.ProjectDetailScreen
import com.example.accountsmanagerapplication.ui.TransactionEditScreen
import com.example.accountsmanagerapplication.ui.LogViewerScreen

sealed interface Destinations {
    data object Dashboard : Destinations { const val route = "dashboard" }
    data object CreateProject : Destinations { const val route = "create_project" }
    data object ProjectDetail : Destinations { const val route = "project_detail/{projectId}" }
    data object AddEditTransaction : Destinations { const val route = "project/{projectId}/tx" }
    data object AddTransaction : Destinations { const val route = "project/{projectId}/add_transaction?type={transactionType}" }
    data object EditTransaction : Destinations { const val route = "project/{projectId}/edit_transaction/{transactionId}" }
    data object LogViewer : Destinations { const val route = "log_viewer" }
}

@Composable
fun AppNavHost(startDestination: String = Destinations.Dashboard.route) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Destinations.CreateProject.route) {
            CreateProjectScreen(navController = navController)
        }
        composable(
            route = Destinations.ProjectDetail.route,
            arguments = listOf(navArgument("projectId") { type = NavType.LongType })
        ) {
            ProjectDetailScreen(navController = navController)
        }
        composable(
            route = Destinations.AddEditTransaction.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("txId") { type = NavType.LongType; defaultValue = -1L }
            )
        ) {
            // TODO: Add/Edit transaction screen
        }

        composable(
            route = Destinations.AddTransaction.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("transactionType") { type = NavType.StringType; defaultValue = "income" }
            )
        ) {
            TransactionEditScreen(navController = navController)
        }

        composable(
            route = Destinations.EditTransaction.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType },
                navArgument("transactionId") { type = NavType.LongType }
            )
        ) {
            TransactionEditScreen(navController = navController)
        }
        
        composable(Destinations.LogViewer.route) {
            LogViewerScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}


