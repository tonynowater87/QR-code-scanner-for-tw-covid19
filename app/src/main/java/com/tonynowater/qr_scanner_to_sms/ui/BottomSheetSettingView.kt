package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tonynowater.qr_scanner_to_sms.ui.theme.SECONDARY_TEXT_COLOR

@ExperimentalMaterialApi
@Composable
fun BottomSheetSettingView() {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .height(200.dp)
    ) {
        Text(text = "Hello from sheet", color = SECONDARY_TEXT_COLOR)
    }
}