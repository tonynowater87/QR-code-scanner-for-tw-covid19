package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import com.tonynowater.qr_scanner_to_sms.ui.theme.QRScannerToSmsTheme
import kotlinx.coroutines.launch


@ExperimentalMaterialApi
@ExperimentalComposeApi
@Composable
fun ContentView() {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    ProvideWindowInsets {
        QRScannerToSmsTheme {
            BottomSheetScaffold(
                scaffoldState = bottomSheetScaffoldState,
                sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                sheetPeekHeight = 0.dp,
                sheetContent = {
                    BottomSheetSettingView()
                }
            ) {
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

                    // toolbar
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
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.5F)
                            .statusBarsPadding(),
                        contentAlignment = Alignment.Center
                    ) {
                        RoundedCornerView()
                    }

                    Box(
                        modifier = Modifier
                            .systemBarsPadding()
                            .padding(24.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Image(
                            painter = painterResource(id = android.R.drawable.arrow_up_float),
                            contentDescription = "設定",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        } else {
                                            bottomSheetScaffoldState.bottomSheetState.collapse()
                                        }
                                    }
                                })
                    }
                }
            }
        }
    }
}