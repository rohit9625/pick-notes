package com.app.picknotes.screens.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.Graph
import com.app.picknotes.R
import com.app.picknotes.models.Note
import com.app.picknotes.models.NoteResponse
import com.app.picknotes.screens.AppBottomBar
import com.app.picknotes.screens.AppTopBar
import com.app.picknotes.screens.PopUpDialog
import com.app.picknotes.ui.theme.PickNotesTheme
import com.app.picknotes.utils.NetworkResult
import com.app.picknotes.auth.presentation.viewmodel.SignInViewModel
import com.app.picknotes.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel<MainViewModel>(),
    authViewModel: SignInViewModel = hiltViewModel<SignInViewModel>()
) {
    val notesState by mainViewModel.notesState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackHostState = remember { SnackbarHostState() }
    val showDialog = remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<NoteResponse?>(null) }

    ModalNavigationDrawer(
        drawerContent = { ModalDrawerSheet {
            DrawerContent(onSignOut = {
                authViewModel.signOut {
                    mainViewModel.deleteAllNotes()
                    navController.navigate(Graph.AUTH) {
                        popUpTo(Graph.MAIN) {
                            inclusive = true
                        }
                    }
                }
            })
        } },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackHostState) },
            topBar = {
                AppTopBar(title = "My Notes", onProfileClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            })
            },
            bottomBar = { AppBottomBar(navController = navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("new_note")
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }}
        ) {padding->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (val state = notesState) {
                    is NetworkResult.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    is NetworkResult.Success -> {
                        state.data?.let {notes->
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(notes) {
                                    NoteCard(
                                        title = it.title,
                                        description = it.description,
                                        onDelete = {
                                            noteToDelete = it
                                            showDialog.value = true
                                        },
                                        onEdit = {
                                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "note",
                                                value = Note(
                                                    id = it._id,
                                                    title = it.title,
                                                    description = it.description
                                                )
                                            )
                                            navController.navigate("new_note")
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                    is NetworkResult.Error -> {
                        state.message?.let {message->
                            scope.launch {
                                snackHostState.showSnackbar(
                                    message = message,
                                    actionLabel = "retry",
                                    duration = SnackbarDuration.Long,
                                    withDismissAction = true
                                )
                            }
                        }
                    }
                }

                if(showDialog.value) {
                    PopUpDialog(
                        onConfirm = {
                            mainViewModel.deleteNote(noteToDelete?._id!!)
                            showDialog.value = false
                        },
                        onCancel = { showDialog.value = false }
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(onSignOut:() -> Unit) {
    Text(
        "Profile",
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
    Divider()
    Spacer(modifier = Modifier.height(32.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color.LightGray)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Name: Rohit")
            Text(text = "Usernam: ronny001")
            Text(text = "Email: rv17837@gmail.com")
            Text(text = "")
        }
        OutlinedButton(
            onClick = { onSignOut() },
        ) {
            Text(text = "Logout", color = Color.Red)
        }
    }
}

@Composable
fun NoteCard(
    title: String,
    description: String,
    onDelete: ()-> Unit,
    onEdit: ()-> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onEdit() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { onDelete() }
                )
            }
            Column(
                modifier = modifier
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    PickNotesTheme {
//        NoteCard(title = "My Title", description = "This is my description for notes app with title - My Title")
        MainScreen(rememberNavController())
    }
}
