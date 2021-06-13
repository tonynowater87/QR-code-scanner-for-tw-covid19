package com.tonynowater.qr_scanner_to_sms

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.tonynowater.qr_scanner_to_sms.ui.ContentView
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeApi
class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainViewModel>()

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                setContent {
                    ContentView(vm)
                }
            } else {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        cameraResult.launch(Manifest.permission.CAMERA)
    }
}

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ContentView()
}