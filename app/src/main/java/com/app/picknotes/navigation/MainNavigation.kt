package com.app.picknotes.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.picknotes.Graph
import com.app.picknotes.notes.domain.Note
import com.app.picknotes.screens.main.AboutScreen
import com.app.picknotes.screens.main.FeedbackScreen
import com.app.picknotes.notes.presentation.MainScreen
import com.app.picknotes.notes.presentation.NewNoteScreen
import com.app.picknotes.screens.main.Screen

fun NavGraphBuilder.mainNavigation(navController: NavController) {
    navigation(route = Graph.MAIN, startDestination = Screen.Notes.route) {
        composable(route = Screen.Notes.route) {
            MainScreen(navController = navController)
            navController.currentBackStackEntry?.savedStateHandle?.remove<Note>("note")
        }
        composable(route = Screen.Feedback.route) {
            FeedbackScreen(navController = navController)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController = navController)
        }
        composable(route = "new_note") {
            val note = navController.previousBackStackEntry?.savedStateHandle?.get<Note>("note")
            NewNoteScreen(navController = navController, note = note)
        }
    }
}