package com.tonynowater.qr_scanner_to_sms

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonynowater.qr_scanner_to_sms.ui.CameraPreview
import com.tonynowater.qr_scanner_to_sms.ui.QRCodeAnalyzer
import com.tonynowater.qr_scanner_to_sms.ui.theme.QRScannerToSmsTheme

class MainActivity : ComponentActivity() {

    val cameraResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            setContent {
                QRScannerToSmsTheme {
                    Box {
                        CameraPreview(analyzer = QRCodeAnalyzer())
                        Text(
                            text = "實聯制 QRCode 掃描小工具",
                            modifier = Modifier.fillMaxWidth(1F).padding(top = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
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
        Box {
            CameraPreview(analyzer = QRCodeAnalyzer())
            Text(text = "Hello!", color = Color.Red)
        }
    }
}