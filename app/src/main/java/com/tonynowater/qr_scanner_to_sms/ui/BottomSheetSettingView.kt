package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.ui.theme.ACCENT_COLOR
import com.tonynowater.qr_scanner_to_sms.ui.theme.SECONDARY_TEXT_COLOR
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun BottomSheetSettingView(vm: MainViewModel) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(200.dp)
    ) {
        Text(text = "功能設定", modifier = Modifier.fillMaxWidth(), color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        simpleFlowRow(
            modifier = Modifier.padding(0.dp),
            horizontalGap = 4.dp,
            verticalGap = 4.dp,
            content = {
                for (tag in settingsTag) {
                    Text(
                        text = "#$tag",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .background(
                                if (vm.vibration) ACCENT_COLOR else SECONDARY_TEXT_COLOR,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(4.dp)
                            .clickable {
                                coroutineScope.launch {
                                    vm.updateVibrate()
                                }
                            }
                    )
                }
            }
        )
    }
}

val settingsTag = listOf(
    "震動"
)