package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.tonynowater.qr_scanner_to_sms.model.QRCodeModel
import com.tonynowater.qr_scanner_to_sms.ui.theme.SECONDARY_TEXT_COLOR

@Composable
fun QrCodeInfoDialog(qrCodeModel: QRCodeModel, onDismiss: () -> Unit) {
    AlertDialog(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
        shape = MaterialTheme.shapes.large,
        onDismissRequest = { onDismiss.invoke() },
        properties = DialogProperties(),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "注意！",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red.copy(alpha = 0.5F)
                )
                Text(
                    "掃描到非實聯制的QRCode",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold, color = Color.Red.copy(alpha = 0.5F)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        qrCodeModel.getTypeDisplayName(),
                        modifier = Modifier
                            .background(
                                qrCodeModel.getTypeDisplayColor(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp),
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    SelectionContainer {
                        Text(
                            "${qrCodeModel.rawValue}",
                            style = MaterialTheme.typography.body1,
                            color = SECONDARY_TEXT_COLOR,
                            modifier = Modifier
                                .padding(2.dp)
                                .border(
                                    BorderStroke(1.dp, qrCodeModel.getTypeDisplayColor()),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(4.dp)
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 20.dp),
                    text = "小提醒：以上框框內的文字可以長按選取複製",
                    color = SECONDARY_TEXT_COLOR,
                    style = MaterialTheme.typography.caption
                )
            }
        },
        confirmButton = { },
        dismissButton = { }
    )
}