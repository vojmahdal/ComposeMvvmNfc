package com.example.composemvvmnfc

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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
    textSave: (String) -> Unit,
    operationSave: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        var textState by remember {
            mutableStateOf("")
        }
        val onTextChange = { mtext: String ->
            textState = mtext
        }

        var operationState by remember {
            mutableStateOf("")
        }
        val onOperationChange = { mtext: String ->
            operationState = mtext
        }

        Switch(
            checked = isLoading,
            onCheckedChange = { switchChange() }
        )
        Text(text = "Read/Write")


        WriteText(
            text = textState,
            onTextChange = onTextChange
        )
        Text(
            text,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.h3
        )

        Button(
            onClick = {
                operationSave(operationState)
                textSave(textState)
            }
        ) {
            Text("Write!")
        }
        Operation(selectedOption = operationState, onOptionSelected = onOperationChange)
    }
}

@Composable
fun Operation(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val radioWrite = listOf("text", "url", "phone", "email")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        radioWrite.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                        }
                    )
                    .padding(horizontal = 16.dp)
            ) {
                RadioButton(
                    selected = (selectedOption == text),
                    onClick = {
                        onOptionSelected(text)
                    }
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

        }
    }
}

@Composable
fun WriteText(
    text: String,
    onTextChange: (String) -> Unit,

    ) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    )
    {
        TextField(
            value = text,
            onValueChange = { onTextChange(it) },
            label = { Text("Enter text") },
            placeholder = { Text(text = "text") },
        )
    }
}