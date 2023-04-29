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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

        if (Build.VERSION.SDK_INT < 30) {
            pendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
        } else {
            pendingIntent = PendingIntent.getActivity(
                this,
                0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            )
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
                        textSave = { model.textSave(it) },
                        operationSave = { model.operationSave(it) })
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
        if (model.isLoading) {
            intentFilterArray = nfcLib.onCreateFilterWrite()
        }
        super.onNewIntent(intent)
        if (model.isLoading) {
            if (model.operation == "text") {
                nfcLib.writeText(intent, model.text)

            } else if (model.operation == "url") {
                nfcLib.writeUrl(intent, model.text)

            } else if (model.operation == "phone") {
                nfcLib.writeTelNumber(intent, model.text)

            } else if (model.operation == "email") {
                nfcLib.writeEmailAddress(intent, model.text)

            } else {
                Toast.makeText(
                    applicationContext,
                    "Invalid operation", Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            intentFilterArray = nfcLib.onCreateFilterRead()

            Toast.makeText(
                applicationContext,
                nfcLib.read(intent),
                Toast.LENGTH_SHORT
            ).show()
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
            textSave = { model.textSave(it) },
            operationSave = { model.operationSave(it) })

    }
}



