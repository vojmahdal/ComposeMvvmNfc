package com.example.composemvvmnfc

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.composemvvmnfc.ui.theme.ComposeMvvmNfcTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easynfc.EzNfc

class MainActivity : ComponentActivity() {
    private lateinit var model: NfcViewModel


    private var intentFilterArray: Array<IntentFilter>? = null
    private var pendingIntent: PendingIntent? = null
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var nfcLib = EzNfc(this, intentFilterArray)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(this).get(NfcViewModel::class.java)

        nfcLib.nfcAdapter = nfcAdapter

        if (Build.VERSION.SDK_INT < 30){
            pendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)}
        else {
            pendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE)
        }

        intentFilterArray = nfcLib.onCreateFilterRead()



        setContent {
            ComposeMvvmNfcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    MainScreen(text = model.text,
                        isLoading = model.isLoading,
                        switchChange = { model.switchChange() },
                        textSave = {model.textSave(it)})

                     }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        nfcLib.onResume(pendingIntent)
    }

    override fun onPause() {
        super.onPause()
        nfcLib.onPause()
    }
    override fun onNewIntent(intent: Intent) {
        val model: NfcViewModel by viewModels()
        if(model.isLoading) {
            intentFilterArray = nfcLib.onCreateFilterWrite()
        }
        super.onNewIntent(intent)
        if(model.isLoading) {
            nfcLib.writeText(intent, model.text)
        }else{
            intentFilterArray = nfcLib.onCreateFilterRead()

            Toast.makeText(applicationContext,
               nfcLib.read(intent),
               Toast.LENGTH_SHORT).show()
        }

    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview(model: NfcViewModel = viewModel()) {
    ComposeMvvmNfcTheme {
        MainScreen(text = model.text,
            isLoading = model.isLoading,
            switchChange = { model.switchChange() },
            textSave = {model.textSave(it)})

    }
}

@Composable
fun ScreenSetupMenu(viewModel: NfcViewModel = viewModel()){
    MainScreen(text = viewModel.text,
        isLoading = viewModel.isLoading,
        switchChange = { viewModel.switchChange() },
        textSave = {viewModel.textSave(it)}
    )
}


@Composable
fun Operation(
    selectedOption: String,
    onTextChange: (String) -> Unit
){
    val radioWrite = listOf("text", "url", "phone", "email")

    val (selectedOption, onOptionSelected) =
        remember{ mutableStateOf(radioWrite[1] ) }

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
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text)
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
    isLoading: Boolean,
    switchChange: () -> Unit,
    onTextChange: (String) -> Unit,

){
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    )
    {
        TextField(value = text,
            onValueChange = {onTextChange(it)},
            label = {Text("Enter text")},
            placeholder = { Text(text = "text") },
        )
    }
}
