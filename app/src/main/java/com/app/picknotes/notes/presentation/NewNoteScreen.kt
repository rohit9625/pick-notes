package com.app.picknotes.notes.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.R
import com.app.picknotes.notes.domain.Note
import com.app.picknotes.ui.theme.PickNotesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(
    uiState: NewNoteState,
    onEvent: (NewNoteEvent) -> Unit,
    navController: NavController
) {

//    LaunchedEffect(key1 = note) {
//        if(note != null) {
//            mainViewModel.editNote(note.title, note.description)
//        }
//    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(if(uiState.id != null) R.string.edit_note else R.string.create_new_note),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                actions = {
                    IconButton(onClick = {
                        onEvent(NewNoteEvent.OnSaveNote)
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = "Done",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 16.dp, end = 16.dp
            )
        ) {
            BasicTextField(
                value = uiState.title,
                onValueChange = { onEvent(NewNoteEvent.OnTitleChange(it)) },
                textStyle = MaterialTheme.typography.titleLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
            ) { innerTextField->
                Box {
                    if(uiState.title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Normal
                        )
                    }
                    innerTextField()
                }
            }

            BasicTextField(
                value = uiState.description,
                onValueChange = { onEvent(NewNoteEvent.OnDescriptionChange(it)) },
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface)
            ) { innerTextField->
                Box {
                    if(uiState.description.isEmpty()) {
                        Text(
                            text = "Note",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun NewNoteScreenPreview() {
    PickNotesTheme {
        NewNoteScreen(
            uiState = NewNoteState(),
            onEvent = { },
            navController = rememberNavController()
        )
    }
}