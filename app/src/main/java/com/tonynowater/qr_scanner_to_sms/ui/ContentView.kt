package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import com.tonynowater.qr_scanner_to_sms.BuildConfig
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.R
import com.tonynowater.qr_scanner_to_sms.ui.theme.DARK_PRIMARY_COLOR
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

    // add lifecycle listener
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                vm?.enableTorch(false)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

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

                if (vm?.qrCodeModel != null) {
                    QrCodeInfoDialog(
                        vm = vm,
                        onDismiss = {
                            vm.scannedInvalidQRCode(null)
                        }
                    )
                }

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
                ) {

                    if (vm!!.enableCameraPermission) {
                        CameraPreviewView(
                            modifier = Modifier
                                .fillMaxSize()
                                .shadow(1.dp, RoundedCornerShape(20.dp)),
                            vm = vm,
                            enableTorch = vm.enableTorch
                        )
                    }

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
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .statusBarsPadding()
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        vm.enableAllBarCodeFormat?.let { enableAllBarCodeFormat ->
                            if (enableAllBarCodeFormat) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "萬用條碼掃描器",
                                        color = Color.White,
                                        modifier = Modifier.wrapContentSize(),
                                        style = MaterialTheme.typography.h5
                                    )
                                    Text(
                                        text = "也支援簡訊實聯制QRCode",
                                        color = Color.White,
                                        modifier = Modifier.wrapContentSize(),
                                        style = MaterialTheme.typography.h6
                                    )
                                }
                            } else {
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
                    }

                    // rounded corner view
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
                        Row(horizontalArrangement = Arrangement.Center) {
                            OutlinedButton(
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = DARK_PRIMARY_COLOR),
                                shape = MaterialTheme.shapes.medium,
                                border = BorderStroke(1.dp, Color.White),
                                onClick = { vm.enableTorch(vm.enableTorch.not()) }) {
                                Image(
                                    painter = painterResource(id = if (vm.enableTorch) R.drawable.outline_flash_on_24 else R.drawable.outline_flash_off_24),
                                    contentDescription = "開啟閃光燈"
                                )
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            OutlinedButton(
                                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = DARK_PRIMARY_COLOR),
                                shape = MaterialTheme.shapes.medium,
                                border = BorderStroke(1.dp, Color.White),
                                onClick = {
                                    coroutineScope.launch {
                                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                }) {
                                Text(
                                    text = "設定",
                                    color = Color.White,
                                    style = MaterialTheme.typography.button
                                )
                            }
                        }
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