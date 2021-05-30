package com.tonynowater.qr_scanner_to_sms

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tonynowater.qr_scanner_to_sms.ui.CameraPreview
import com.tonynowater.qr_scanner_to_sms.ui.QRCodeAnalyzer
import com.tonynowater.qr_scanner_to_sms.ui.theme.QRScannerToSmsTheme

class MainActivity : ComponentActivity() {

    val cameraResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        Log.d("[DEBUG]", "permission granted = $it")
        if (it) {
            setContent {
                QRScannerToSmsTheme {
                    CameraPreview(analyzer = QRCodeAnalyzer())
                }
            }
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraResult.launch(Manifest.permission.CAMERA)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QRScannerToSmsTheme {
        CameraPreview(analyzer = QRCodeAnalyzer())
    }
}