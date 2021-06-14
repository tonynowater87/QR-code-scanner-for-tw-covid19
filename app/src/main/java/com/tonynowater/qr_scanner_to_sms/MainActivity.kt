package com.tonynowater.qr_scanner_to_sms

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.tonynowater.qr_scanner_to_sms.ui.ContentView
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeApi
class MainActivity : ComponentActivity() {

    private val vm by viewModels<MainViewModel>()

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

            when {
                granted -> {
                    vm.enableCameraPermission()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity, Manifest.permission.CAMERA
                ) -> {
                    finish()
                }
                else -> {
                    Toast.makeText(applicationContext, "請至設定頁手動授與相機權限方可使用此App", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        cameraResult.launch(Manifest.permission.CAMERA)
        setContent {
            ContentView(vm)
        }
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