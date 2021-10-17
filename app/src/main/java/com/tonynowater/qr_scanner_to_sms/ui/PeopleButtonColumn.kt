package com.tonynowater.qr_scanner_to_sms.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tonynowater.qr_scanner_to_sms.MainViewModel
import com.tonynowater.qr_scanner_to_sms.ui.theme.DARK_PRIMARY_COLOR
import kotlinx.coroutines.InternalCoroutinesApi

private const val maxPeopleCount = 101

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@InternalCoroutinesApi
@Composable
fun PeopleButtonColumn(
    isSelectingPeopleCount: Boolean,
    vm: MainViewModel,
    onSelectedPeopleCount: (isSelected: Boolean) -> Unit
) {

    val lazyListState = rememberLazyListState()
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isSelectingPeopleCount) {
            LazyColumn(
                modifier = Modifier.height(250.dp),
                state = lazyListState,
                horizontalAlignment = Alignment.End,
                reverseLayout = true,
                content = {
                    stickyHeader {
                        OutlinedButton(
                            modifier = Modifier.defaultMinSize(200.dp),
                            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = DARK_PRIMARY_COLOR),
                            shape = MaterialTheme.shapes.large,
                            border = BorderStroke(1.dp, Color.White),
                            onClick = {
                                onSelectedPeopleCount.invoke(false)
                            }) {
                            Text(
                                text = "選擇同行人數",
                                color = Color.White,
                                style = MaterialTheme.typography.button
                            )
                        }
                    }
                    items(maxPeopleCount) { index ->
                        Spacer(modifier = Modifier.size(4.dp))
                        PeopleOutLineButton(
                            count = index,
                            onClick = {
                                vm.addPeopleCount(it)
                                onSelectedPeopleCount.invoke(false)
                            })
                    }
                })
        } else {
            PeopleOutLineButton(count = vm.peopleCount, text = "目前同行人數", onClick = {
                onSelectedPeopleCount.invoke(true)
            })
        }
    }
}

@Composable
private fun PeopleOutLineButton(count: Int, text: String = "", onClick: (count: Int) -> Unit) {
    OutlinedButton(
        modifier = Modifier.defaultMinSize(200.dp),
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = DARK_PRIMARY_COLOR),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, Color.White),
        onClick = {
            onClick.invoke(count)
        }) {
        Text(
            text = "$text+$count",
            color = Color.White,
            style = MaterialTheme.typography.button
        )
    }
}