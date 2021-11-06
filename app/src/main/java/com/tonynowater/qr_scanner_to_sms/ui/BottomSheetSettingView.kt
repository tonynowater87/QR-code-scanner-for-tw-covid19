package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

    var rememberHiddenFeature = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(250.dp)
    ) {
        Column(
            modifier = Modifier.padding(0.dp),
            content = {
                Text(
                    text = "功能設定",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .clickable {
                            rememberHiddenFeature.value = rememberHiddenFeature.value + 1
                        },
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 2.dp)
                        .clickable(indication = null, interactionSource = interactionSource) {
                            coroutineScope.launch {
                                vm.updateVibrate()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "掃描實聯制條碼成功時震動",
                        color = SECONDARY_TEXT_COLOR,
                        style = MaterialTheme.typography.button
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Switch(
                        checked = vm.vibration,
                        onCheckedChange = null
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 2.dp)
                        .clickable(indication = null, interactionSource = interactionSource) {
                            coroutineScope.launch {
                                vm.updateFinishAfterScanned()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "掃描實聯制條碼成功後自動關閉App",
                        color = SECONDARY_TEXT_COLOR,
                        style = MaterialTheme.typography.button
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Switch(
                        checked = vm.finishAfterScanned,
                        onCheckedChange = null
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 2.dp)
                            .clickable(indication = null, interactionSource = interactionSource) {
                                coroutineScope.launch {
                                    vm.updateEnableSaveLastPeopleCount()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "記住最後掃描實聯制成功的同行人數",
                            color = SECONDARY_TEXT_COLOR,
                            style = MaterialTheme.typography.button
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Switch(
                            checked = vm.enableSaveLastPeopleCount,
                            onCheckedChange = null
                        )
                    }
                    Text(
                        text = "(每次開啟後會回復上次掃描時的同行人數)",
                        color = SECONDARY_TEXT_COLOR,
                        style = MaterialTheme.typography.caption
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    Row(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(vertical = 2.dp)
                            .clickable(indication = null, interactionSource = interactionSource) {
                                coroutineScope.launch {
                                    vm.updateEnableAllBarCodeFormat()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "啟用支援掃描以下所有格式條碼",
                            color = SECONDARY_TEXT_COLOR,
                            style = MaterialTheme.typography.button
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Switch(
                            checked = vm.enableAllBarCodeFormat ?: false,
                            onCheckedChange = null
                        )
                    }
                    Text(
                        text = "(Code128,Code39,Code93,Codabar,DataMatrix,EAN-13,EAN-8,ITF,UPC-A,UPC-E,QR,AZTEC,PDF417)",
                        color = SECONDARY_TEXT_COLOR,
                        style = MaterialTheme.typography.caption
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(vertical = 2.dp)
                        .alpha(if (rememberHiddenFeature.value > 5) 1.0F else 0F)
                        .clickable(
                            enabled = rememberHiddenFeature.value > 5,
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            coroutineScope.launch {
                                vm.updateRoundedCornerAnimation()
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "掃描框框動畫",
                        color = SECONDARY_TEXT_COLOR,
                        style = MaterialTheme.typography.button
                    )
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