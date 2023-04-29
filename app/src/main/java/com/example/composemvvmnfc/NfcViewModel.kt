package com.example.composemvvmnfc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NfcViewModel : ViewModel() {


    var text by mutableStateOf("")
    var operation by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    fun textSave(txt: String){
        text = txt
    }




    fun switchChange(){
        isLoading = !isLoading
    }

}