package com.app.picknotes.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title : String, val icon: ImageVector) {
    data object Notes: Screen(route = "notes", title = "Notes", Icons.Default.Home)
    data object Feedback : Screen(route = "feedback", title = "Feedback", Icons.Default.ThumbUp)
    data object About: Screen(route = "about", title = "About", Icons.Default.Info)
}