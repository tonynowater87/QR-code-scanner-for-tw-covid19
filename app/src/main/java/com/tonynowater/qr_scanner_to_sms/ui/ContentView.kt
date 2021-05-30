package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
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

                CameraPreview(
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

                    val padding = 50.dp
                    val arrowSideLength = 40.dp
                    val screenWidthDp = LocalConfiguration.current.screenWidthDp
                    val rectSideLength = screenWidthDp.dp - padding - padding
                    val startPointY = (screenWidthDp / 2).dp
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = arrowSideLength)
                    ) {

                        val topLeft1 =
                            padding.toPx() to startPointY.toPx() + arrowSideLength.toPx()
                        val topLeft2 = padding.toPx() to startPointY.toPx()
                        val topLeft3 =
                            padding.toPx() + arrowSideLength.toPx() to startPointY.toPx()

                        val topRight1 =
                            screenWidthDp.dp.toPx() - padding.toPx() - arrowSideLength.toPx() to startPointY.toPx()
                        val topRight2 =
                            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx()
                        val topRight3 =
                            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx() + arrowSideLength.toPx()

                        val bottomRight1 =
                            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx() + rectSideLength.toPx() - arrowSideLength.toPx()
                        val bottomRight2 =
                            screenWidthDp.dp.toPx() - padding.toPx() to startPointY.toPx() + rectSideLength.toPx()
                        val bottomRight3 =
                            screenWidthDp.dp.toPx() - padding.toPx() - arrowSideLength.toPx() to startPointY.toPx() + rectSideLength.toPx()

                        val bottomLeft1 =
                            padding.toPx() + arrowSideLength.toPx() to startPointY.toPx() + rectSideLength.toPx()
                        val bottomLeft2 =
                            padding.toPx() to startPointY.toPx() + rectSideLength.toPx()
                        val bottomLeft3 =
                            padding.toPx() to startPointY.toPx() + rectSideLength.toPx() - arrowSideLength.toPx()



                        drawPath(
                            path = Path().apply {
                                // draw topLeft
                                moveTo(topLeft1.first, topLeft1.second)
                                quadraticBezierTo(
                                    topLeft2.first,
                                    topLeft2.second,
                                    topLeft3.first,
                                    topLeft3.second
                                )

                                // draw topRight
                                moveTo(
                                    topRight1.first,
                                    topRight1.second
                                )
                                quadraticBezierTo(
                                    topRight2.first, topRight2.second,
                                    topRight3.first, topRight3.second
                                )

                                // draw bottomRight
                                moveTo(
                                    bottomRight1.first,
                                    bottomRight1.second
                                )
                                quadraticBezierTo(
                                    bottomRight2.first, bottomRight2.second,
                                    bottomRight3.first, bottomRight3.second
                                )

                                // draw bottomLeft
                                moveTo(
                                    bottomLeft1.first,
                                    bottomLeft1.second
                                )
                                quadraticBezierTo(
                                    bottomLeft2.first, bottomLeft2.second,
                                    bottomLeft3.first, bottomLeft3.second
                                )
                            },
                            color = Color.White,
                            style = Stroke(width = 8f)
                        )
                    }
                }
            }
        }
    }
}