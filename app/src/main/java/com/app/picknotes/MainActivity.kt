package com.app.picknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.navigation.authNavigation
import com.app.picknotes.navigation.mainNavigation
import com.app.picknotes.ui.theme.PickNotesTheme
import com.app.picknotes.utils.NetworkUtils
import com.app.picknotes.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            PickNotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PickNotesApp(
                        navController = navController,
                        tokenManager = tokenManager
                    )
                }
            }
        }
    }
}
@Composable
fun PickNotesApp(navController: NavHostController, tokenManager: TokenManager) {
    val startDest = if (tokenManager.getToken() != null) Graph.MAIN else Graph.AUTH

    NavHost(navController = navController, startDestination = startDest) {
        authNavigation(navController = navController)

        mainNavigation(navController = navController)
    }
}

object Graph{
    const val AUTH = "auth"
    const val MAIN = "main"
}