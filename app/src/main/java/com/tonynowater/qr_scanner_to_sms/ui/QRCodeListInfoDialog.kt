package com.tonynowater.qr_scanner_to_sms.ui

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.model.QRCodeModel
import com.tonynowater.qr_scanner_to_sms.ui.theme.LIGHT_PRIMARY_COLOR
import com.tonynowater.qr_scanner_to_sms.ui.theme.SECONDARY_TEXT_COLOR
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(
    InternalCoroutinesApi::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun QrCodeListInfoDialog(vm: MainViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val qrCodeModelList = vm.qrCodeModelList

    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .heightIn(0.dp, configuration.screenHeightDp.dp - 200.dp)
                    .background(LIGHT_PRIMARY_COLOR, RoundedCornerShape(16.dp))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "掃描成功",
                        modifier = Modifier.padding(top = 16.dp),
                        color = SECONDARY_TEXT_COLOR,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                    )
                    LazyColumn(
                        modifier = Modifier.padding(
                            top = 8.dp,
                            bottom = 8.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(count = qrCodeModelList.size) { index ->
                            val qrCodeModel = qrCodeModelList[index]
                            qrCodeRowView(index, context, qrCodeModel)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun qrCodeRowView(index: Int, context: Context, qrCodeModel: QRCodeModel) {
    Row(modifier = Modifier.padding(all = 4.dp)) {
        Text(
            text = "${index + 1}.",
            modifier = Modifier
                .padding(4.dp)
                .selectableGroup(),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
                    .border(
                        BorderStroke(1.dp, qrCodeModel.getTypeDisplayColor()),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(4.dp)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = qrCodeModel.getTypeDisplayName(),
                        modifier = Modifier
                            .background(
                                qrCodeModel.getTypeDisplayColor(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(6.dp),
                        style = MaterialTheme.typography.body1.copy(color = LIGHT_PRIMARY_COLOR)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    SelectionContainer {
                        Text(
                            qrCodeModel.rawValue,
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.body1,
                            color = SECONDARY_TEXT_COLOR
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (qrCodeModel.actionText() != "複製內容") {
                    Button(onClick = {
                        qrCodeModel.copy(context)
                    }) {
                        Text(
                            text = "複製內容",
                            color = LIGHT_PRIMARY_COLOR,
                            style = MaterialTheme.typography.button
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                }
                Button(onClick = {
                    qrCodeModel.action(context)
                }) {
                    Text(
                        text = qrCodeModel.actionText(),
                        color = LIGHT_PRIMARY_COLOR,
                        style = MaterialTheme.typography.button
                    )
                }
            }
        }
    }
}