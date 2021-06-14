package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.rememberLottieAnimationState
import com.google.accompanist.insets.*
import com.tonynowater.qr_scanner_to_sms.BuildConfig
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.R
import com.tonynowater.qr_scanner_to_sms.ui.theme.QRScannerToSmsTheme
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


@InternalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeApi
@Composable
fun ContentView(vm: MainViewModel? = null) {

    val interactionSource = remember { MutableInteractionSource() }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    val draggableEnableHeight = LocalConfiguration.current.screenHeightDp.dp / 4 * 3

    val animationSpec = remember { LottieAnimationSpec.RawRes(R.raw.swipe_up_gesture) }
    val animationState = rememberLottieAnimationState(
        autoPlay = true,
        repeatCount = Integer.MAX_VALUE
    )

    ProvideWindowInsets {
        QRScannerToSmsTheme {
            BottomSheetScaffold(
                scaffoldState = bottomSheetScaffoldState,
                sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                sheetPeekHeight = 0.dp,
                sheetContent = {
                    BottomSheetSettingView(vm!!)
                }
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .clickable(indication = null, interactionSource = interactionSource) {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            val (posX, posY) = change.position
                            val (dragX, dragY) = dragAmount
                            when {
                                posY > draggableEnableHeight.toPx() && dragY < 0 -> {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isAnimationRunning) {
                                            return@launch
                                        }
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                }
                            }
                        }
                    }) {

                    CameraPreviewView(
                        modifier = Modifier
                            .fillMaxSize()
                            .shadow(1.dp, RoundedCornerShape(20.dp)),
                        vm = vm!!
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
                        Row(
                            Modifier.padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "實聯制",
                                color = Color.White,
                                modifier = Modifier.wrapContentSize(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h5
                            )
                            Text(
                                text = " QRCode掃描小工具",
                                color = Color.White,
                                modifier = Modifier.wrapContentSize(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.5F)
                            .statusBarsPadding(),
                        contentAlignment = Alignment.Center
                    ) {
                        RoundedCornerView(vm)
                    }

                    Box(
                        modifier = Modifier
                            .systemBarsPadding()
                            .padding(24.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        LottieAnimation(
                            spec = animationSpec,
                            animationState = animationState,
                            modifier = Modifier
                                .size(80.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource
                                ) {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                }
                        )
                        Text(
                            text = "設定請點這裡",
                            color = Color.White,
                            modifier = Modifier
                                .wrapContentSize()
                                .offset(y = 4.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = interactionSource
                                ) {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                },
                            style = MaterialTheme.typography.caption
                        )
                    }

                    Box(
                        modifier = Modifier
                            .systemBarsPadding()
                            .padding(4.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            text = "v${BuildConfig.VERSION_NAME}",
                            color = Color.White,
                            modifier = Modifier.wrapContentSize(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        }
    }
}