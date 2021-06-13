package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.ui.theme.SECONDARY_TEXT_COLOR
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
@ExperimentalMaterialApi
@Composable
fun BottomSheetSettingView(vm: MainViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

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
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable(indication = null, interactionSource = interactionSource) {
                            coroutineScope.launch {
                                vm.updateVibrate()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "成功掃描時震動", color = SECONDARY_TEXT_COLOR)
                    Spacer(modifier = Modifier.size(2.dp))
                    Switch(
                        checked = vm.vibration,
                        onCheckedChange = null
                    )
                }

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .clickable(indication = null, interactionSource = interactionSource) {
                            coroutineScope.launch {
                                vm.updateRoundedCornerAnimation()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "掃描框框動畫", color = SECONDARY_TEXT_COLOR)
                    Spacer(modifier = Modifier.size(2.dp))
                    Switch(
                        checked = vm.roundedCornerAnimate,
                        onCheckedChange = null
                    )
                }
            }
        )
    }
}