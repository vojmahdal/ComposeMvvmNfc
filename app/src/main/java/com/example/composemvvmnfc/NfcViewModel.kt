package com.example.composemvvmnfc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NfcViewModel : ViewModel() {
    var text by mutableStateOf("")
    var operation by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun textSave(txt: String) {
        text = txt
    }

    fun operationSave(txt: String) {
        operation = txt
    }

    fun switchChange() {
        isLoading = !isLoading
    }
}