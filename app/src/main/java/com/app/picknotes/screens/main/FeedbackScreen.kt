package com.app.picknotes.screens.main

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.screens.AppBottomBar
import com.app.picknotes.screens.AppTopBar
import com.app.picknotes.ui.theme.PickNotesTheme

@Composable
fun FeedbackScreen(navController: NavController) {
    var feedback by remember { mutableStateOf("") }
    val maxChar = 400
    val isError by remember {
        derivedStateOf { feedback.length > maxChar }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Feedback") {
        }},
        bottomBar = { AppBottomBar(navController = navController) },
    ) {padding->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    top = padding.calculateTopPadding(),
                    end = 16.dp,
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            BasicTextField(
                value = feedback,
                onValueChange = { feedback = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .border(
                        width = 1.dp,
                        color = if (!isError) MaterialTheme.colorScheme.primary else Color.Red,
                        shape = RoundedCornerShape(8.dp)
                    )

            ) {innerTextField->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 128.dp)
                            .weight(3f)
                    ) {
                        if(feedback.isEmpty()) {
                            Text(
                                text = "Start Typing...",
                                color = Color.Gray,
                            )
                        }
                        innerTextField()
                    }
                    Text(
                        text = "${feedback.length}/$maxChar",
                        modifier = Modifier
                            .align(Alignment.End)
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /*TODO*/ },
                enabled = !isError,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Submit")
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun FeedbackScreenPreview() {
    PickNotesTheme {
        FeedbackScreen(rememberNavController())
    }
}