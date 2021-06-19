package com.tonynowater.qr_scanner_to_sms

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tonynowater.qr_scanner_to_sms.utils.dataStoreProtobuf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

@InternalCoroutinesApi
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStoreProtobuf = app.dataStoreProtobuf

    var enableCameraPermission: Boolean by mutableStateOf(false)
        private set

    var settingPreference: SettingsPreference by mutableStateOf(SettingsPreference.getDefaultInstance())
        private set

    init {
        Log.d("[DEBUG]", "vm init: ")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreProtobuf
                .data
                .catch {
                    if (it is IOException) {
                        emit(SettingsPreference.getDefaultInstance())
                    } else {
                        throw it
                    }
                }
                .collect(object : FlowCollector<SettingsPreference> {
                    override suspend fun emit(value: SettingsPreference) {
                        Log.d("[DEBUG]", "protobuf")
                        Log.d("[DEBUG]", "isVibration = ${value.isVibration}")
                        Log.d(
                            "[DEBUG]",
                            "isFinishingAppAfterScan = ${value.isFinishingAppAfterScan}"
                        )
                        Log.d("[DEBUG]", "isEnableAnimation = ${value.isEnableAnimation}")
                        settingPreference = value
                    }
                })
        }
    }

    fun enableCameraPermission() {
        enableCameraPermission = true
    }

    suspend fun updateVibrate() {
        dataStoreProtobuf.updateData {
            it.toBuilder().setIsVibration(!it.isVibration).build()
        }
    }

    suspend fun updateRoundedCornerAnimation() {
        dataStoreProtobuf.updateData {
            it.toBuilder().setIsEnableAnimation(!it.isEnableAnimation).build()
        }
    }

    suspend fun updateFinishAfterScanned() {
        dataStoreProtobuf.updateData {
            it.toBuilder().setIsFinishingAppAfterScan(!it.isFinishingAppAfterScan).build()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}