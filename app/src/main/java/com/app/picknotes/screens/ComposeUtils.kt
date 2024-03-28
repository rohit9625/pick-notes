package com.app.picknotes.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.screens.main.Screen
import com.app.picknotes.ui.theme.PickNotesTheme

@Composable
fun TextFieldWithIcons(
    text: String,
    label: String,
    errorMessage: String?,
    onValueChange: (String)-> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            isError = errorMessage != null,
            label = { Text(text = label) },
            modifier = modifier,
            keyboardOptions = keyboardOptions,
            singleLine = true
        )
        if(errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.End)
                    .height(16.dp)
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(title: String, onProfileClick: ()-> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(36.dp)
                    .clickable { onProfileClick() }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
    )
}

@Composable
fun AppBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = navBackStackEntry?.destination
    val screens = listOf(Screen.Notes, Screen.Feedback, Screen.About)

    NavigationBar {
       for(screen in screens) {
           NavigationBarItem(
               selected = currentScreen?.hierarchy?.any {
                   it.route == screen.route
               } == true,
               onClick = {
                   navController.navigate(screen.route){
                       launchSingleTop = true
                       popUpTo(Screen.Notes.route)
                   }
               },
               icon = {
                   Icon(
                       imageVector = screen.icon,
                       contentDescription = null,
                       modifier = Modifier.size(24.dp)
                   )
               },
               label = {
                   Text(text = screen.title)
               }
           )
       }
    }
}


@Composable
fun PopUpDialog(
    onConfirm: ()-> Unit,
    onCancel: ()-> Unit
) {
    AlertDialog(
        onDismissRequest =  onCancel,
        title = {
            Text(text = "Delete Confirmation")
        },
        text = {
            Text(text = "Are you sure you want to delete?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onCancel()
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PopUpDialogPreview() {
    PickNotesTheme {
        PopUpDialog(onCancel = {}, onConfirm = {})
    }
}

@Preview
@Composable
fun AppTopBarPreview() {
    AppTopBar(title = "Preview") {

    }
}

@Preview
@Composable
fun AppBottomBarPreview() {
    AppBottomBar(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun TextFieldWithIconsPreview() {
    TextFieldWithIcons(
        text = "",
        label = "Email",
        onValueChange = {},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        errorMessage = null
    )
}