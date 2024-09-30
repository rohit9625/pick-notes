package com.app.picknotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.app.picknotes.auth.presentation.SignInScreen
import com.app.picknotes.auth.presentation.SignUpScreen
import com.app.picknotes.auth.presentation.viewmodel.SignInViewModel
import com.app.picknotes.auth.presentation.viewmodel.SignUpViewModel
import com.app.picknotes.notes.domain.Note
import com.app.picknotes.notes.presentation.MainScreen
import com.app.picknotes.notes.presentation.MainViewModel
import com.app.picknotes.notes.presentation.NewNoteScreen
import com.app.picknotes.notes.presentation.NewNoteViewModel
import com.app.picknotes.utils.TokenManager
import kotlin.reflect.typeOf

@Composable
fun AppNavHost(navController: NavHostController, tokenManager: TokenManager) {
    val startDest = if (tokenManager.getToken() != null) NavRoute.HomeScreen else NavRoute.SignInScreen

    NavHost(navController = navController, startDestination = startDest) {
        composable<NavRoute.HomeScreen> {
            val viewModel = hiltViewModel<MainViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            MainScreen(navController = navController, uiState = uiState)
        }

        composable<NavRoute.NewNoteScreen>(
            typeMap = mapOf(typeOf<Note?>() to NoteNavType)
        ) {
            val args = it.toRoute<NavRoute.NewNoteScreen>()
            val viewModel = hiltViewModel<NewNoteViewModel>()
            args.note?.let { note-> viewModel.initEdit(note) }

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            NewNoteScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<NavRoute.SignInScreen> {
            val viewModel = hiltViewModel<SignInViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SignInScreen(
                state = uiState,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<NavRoute.SignUpScreen> {
            val viewModel = hiltViewModel<SignUpViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            SignUpScreen(
                state = uiState,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }
    }
}