package com.tonynowater.qr_scanner_to_sms

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tonynowater.qr_scanner_to_sms.model.QRCodeModel
import com.tonynowater.qr_scanner_to_sms.utils.dataStore
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

@InternalCoroutinesApi
class MainViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private val vibrateKey = booleanPreferencesKey("vibrateKey")
        private val roundedCornerAnimateKey = booleanPreferencesKey("roundedCornerAnimateKey")
        private val finishAfterScannedKey = booleanPreferencesKey("finishAfterScannedKey")
        private val enableAllBarCodeFormatKey = booleanPreferencesKey("enableAllBarCodeFormat")
    }

    private val dataStore = app.dataStore

    var enableCameraPermission: Boolean by mutableStateOf(false)
        private set

    var qrCodeModel: QRCodeModel? by mutableStateOf(null)
        private set

    var vibration: Boolean by mutableStateOf(true)
        private set

    var roundedCornerAnimate: Boolean by mutableStateOf(false)
        private set

    var finishAfterScanned: Boolean by mutableStateOf(true)
        private set

    var enableAllBarCodeFormat: Boolean? by mutableStateOf(null)
        private set

    var enableTorch: Boolean by mutableStateOf(false)
        private set

    init {
        //Log.d("[DEBUG]", "vm init: ")
        viewModelScope.launch {
            dataStore.data
                .catch {
                    if (it is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map { it[vibrateKey] }
                .collect(object : FlowCollector<Boolean?> {
                    override suspend fun emit(value: Boolean?) {
                        //Log.d("[DEBUG]", "vibration changed = $value")
                        vibration = value ?: true
                    }
                })
        }

        viewModelScope.launch {
            dataStore.data
                .catch {
                    if (it is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map { it[roundedCornerAnimateKey] }
                .collect(object : FlowCollector<Boolean?> {
                    override suspend fun emit(value: Boolean?) {
                        //Log.d("[DEBUG]", "roundedCornerAnimate changed = $value")
                        roundedCornerAnimate = value ?: false
                    }
                })
        }

        viewModelScope.launch {
            dataStore.data
                .catch {
                    if (it is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map { it[finishAfterScannedKey] }
                .collect(object : FlowCollector<Boolean?> {
                    override suspend fun emit(value: Boolean?) {
                        //Log.d("[DEBUG]", "finishAfterScanned changed = $value")
                        finishAfterScanned = value ?: true
                    }
                })
        }

        viewModelScope.launch {
            dataStore.data
                .catch {
                    if (it is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw it
                    }
                }
                .map { it[enableAllBarCodeFormatKey] }
                .collect(object : FlowCollector<Boolean?> {
                    override suspend fun emit(value: Boolean?) {
                        //Log.d("[DEBUG]", "enableAllBarCodeFormat changed = $value")
                        enableAllBarCodeFormat = value ?: false
                    }
                })
        }
    }

    fun enableCameraPermission() {
        enableCameraPermission = true
    }

    suspend fun updateVibrate() {
        dataStore.edit {
            it[vibrateKey] = it[vibrateKey]?.not()
                ?: false // default value is true, and first time will be null, so return false when null
        }
    }

    suspend fun updateRoundedCornerAnimation() {
        dataStore.edit {
            it[roundedCornerAnimateKey] = it[roundedCornerAnimateKey]?.not()
                ?: true // default value is false, and first time will be null, so return true when null
        }
    }

    suspend fun updateFinishAfterScanned() {
        dataStore.edit {
            it[finishAfterScannedKey] = it[finishAfterScannedKey]?.not()
                ?: false // default value is true, and first time will be null, so return false when null
        }
    }

    suspend fun updateEnableAllBarCodeFormat() {
        this.enableTorch = false
        dataStore.edit {
            it[enableAllBarCodeFormatKey] = it[enableAllBarCodeFormatKey]?.not()
                ?: true // default value is false, and first time will be null, so return true when null
        }
    }

    fun scannedInvalidQRCode(qrCodeModel: QRCodeModel?) {
        this.qrCodeModel = qrCodeModel
    }

    fun enableTorch(enableTorch: Boolean) {
        this.enableTorch = enableTorch
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}