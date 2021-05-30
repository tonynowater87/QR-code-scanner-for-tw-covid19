package com.tonynowater.qr_scanner_to_sms

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.tonynowater.qr_scanner_to_sms.ui.CameraPreview
import com.tonynowater.qr_scanner_to_sms.ui.theme.QRScannerToSmsTheme

@ExperimentalComposeApi
class MainActivity : ComponentActivity() {

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                setContent {
                    ProvideWindowInsets {
                        QRScannerToSmsTheme {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CameraPreview(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .shadow(1.dp, RoundedCornerShape(20.dp))
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .statusBarsPadding(),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Text(
                                        text = "實聯制 QRCode 掃描小工具",
                                        modifier = Modifier.fillMaxWidth(1F),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = TextUnit(20F, TextUnitType.Sp)
                                    )
                                    val rectWidth =
                                        (LocalConfiguration.current.screenWidthDp - 80).dp
                                    val rectHeight =
                                        (LocalConfiguration.current.screenHeightDp / 2).dp

                                    Canvas(
                                        modifier = Modifier
                                            .size(
                                                rectWidth,
                                                rectHeight
                                            )
                                            .offset(y = rectHeight / 2)
                                    ) {

                                        drawRoundRect(
                                            color = Color.White,
                                            size = Size(rectWidth.toPx(), rectHeight.toPx()),
                                            cornerRadius = CornerRadius(x = 80F, y = 80F),
                                            style = Stroke(width = 5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QRScannerToSmsTheme {
        Box {
            Text(text = "Hello!", color = Color.Red)
        }
    }
}