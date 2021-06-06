package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.tonynowater.qr_scanner_to_sms.ui.theme.QRScannerToSmsTheme


@ExperimentalComposeApi
@Composable
fun ContentView() {

    ProvideWindowInsets {
        QRScannerToSmsTheme {
            Box(modifier = Modifier.fillMaxSize()) {

                CameraPreviewView(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(1.dp, RoundedCornerShape(20.dp))
                )

                // top mask
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                0.0f to Color.Black,
                                1.0f to Color.Transparent
                            )
                        )
                )

                // bottom mask
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                1.0f to Color.Black
                            )
                        )
                )

                // body
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    contentAlignment = Alignment.TopCenter
                ) {

                    Row(Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "實聯制",
                            color = Color.White,
                            modifier = Modifier.wrapContentSize(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = TextUnit(24F, TextUnitType.Sp)
                        )
                        Text(
                            text = " QRCode掃描小工具",
                            color = Color.White,
                            modifier = Modifier.wrapContentSize(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Normal,
                            fontSize = TextUnit(20F, TextUnitType.Sp)
                        )
                    }
                    RoundedCornerView()
                }
            }
        }
    }
}