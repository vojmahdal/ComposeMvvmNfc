package com.example.composemvvmnfc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    text: String,
    isLoading: Boolean,
    switchChange: () -> Unit,
    textSave: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        var textState by remember {
            mutableStateOf("")
        }
        val onTextChange = { mtext: String ->
            textState = mtext
        }


        Switch(
            checked = isLoading,
            onCheckedChange = { switchChange() }
        )
        Text(text ="Read/Write")


        val openDialog = remember { mutableStateOf(false)  }
        WriteText(
            text = textState,
            isLoading = isLoading,
            switchChange = switchChange,
            onTextChange = onTextChange
        )
        Text(
            text,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.h3
        )
        Button(
            onClick = { textSave(textState) }
        ) {
            Text("Write!")
        }

    }
}