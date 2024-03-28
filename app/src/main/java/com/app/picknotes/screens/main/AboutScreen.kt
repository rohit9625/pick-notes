package com.app.picknotes.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.screens.AppBottomBar
import com.app.picknotes.screens.AppTopBar
import com.app.picknotes.ui.theme.PickNotesTheme

@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = { AppTopBar(title = "About") {

        }},
        bottomBar = { AppBottomBar(navController = navController) }
    ) {padding->
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                top = padding.calculateTopPadding(),
                end = 16.dp,
                bottom = padding.calculateBottomPadding()
            )
        ) {

        }
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    PickNotesTheme {
        AboutScreen(rememberNavController())
    }
}