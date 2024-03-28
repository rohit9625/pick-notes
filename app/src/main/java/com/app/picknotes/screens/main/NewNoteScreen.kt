package com.app.picknotes.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.R
import com.app.picknotes.models.Note
import com.app.picknotes.ui.theme.PickNotesTheme
import com.app.picknotes.viewmodels.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(
    mainViewModel: MainViewModel = hiltViewModel<MainViewModel>(),
    navController: NavController,
    note: Note?
) {

    LaunchedEffect(key1 = note) {
        if(note != null) {
            mainViewModel.editNote(note.title, note.description)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                navController.navigateUp()
                            }
                    )
                },
                title = {
                    Text(
                        text = stringResource(
                            if(note != null) R.string.edit_note else R.string.create_new_note
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                if(note != null) mainViewModel.updateNote(note.id) else mainViewModel.createNote()
                                navController.popBackStack()
                            }
                    )
                }
            )
        }
    ) {padding->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(
                bottom = padding.calculateBottomPadding()
            )
        ) {
            Divider(modifier = Modifier.padding(top = padding.calculateTopPadding()))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                BasicTextField(
                    value = mainViewModel.newNoteTitle,
                    onValueChange = { mainViewModel.updateNewNoteTitle(it) },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ){innerTextField ->
                    Box {
                        if(mainViewModel.newNoteTitle.isEmpty()) {
                            Text(
                                text = "Title",
                                color = Color.Gray,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        innerTextField()
                    }
                }

                BasicTextField(
                    value = mainViewModel.newNoteDescription,
                    onValueChange = { mainViewModel.updateNewNoteDescription(it) },
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ){innerTextField ->
                    Box {
                        if(mainViewModel.newNoteDescription.isEmpty()) {
                            Text(
                                text = "Note",
                                color = Color.Gray,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                        innerTextField()
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NewNoteScreenPreview() {
    PickNotesTheme {
        NewNoteScreen(
            navController = rememberNavController(),
            note = null
        )
    }
}