package com.tonynowater.qr_scanner_to_sms

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tonynowater.qr_scanner_to_sms.utils.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class MainViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        private val vibrateKey = booleanPreferencesKey("vibrateKey")
        private val roundedCornerAnimateKey = booleanPreferencesKey("roundedCornerAnimateKey")
        private val finishAfterScannedKey = booleanPreferencesKey("finishAfterScannedKey")
    }

    private val dataStore = app.dataStore

    var enableCameraPermission: Boolean by mutableStateOf(false)
        private set

    var vibration: Boolean by mutableStateOf(true)
        private set

    var roundedCornerAnimate: Boolean by mutableStateOf(false)
        private set

    var finishAfterScanned: Boolean by mutableStateOf(true)
        private set

    init {
        Log.d("[DEBUG]", "vm init: ")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataStore.data
                    .map { it[vibrateKey] }
                    .collect(object : FlowCollector<Boolean?> {
                        override suspend fun emit(value: Boolean?) {
                            Log.d("[DEBUG]", "vibration changed = $value")
                            vibration = value ?: true
                        }
                    })
            } catch (e: Exception) {
                Log.d("[DEBUG]", "vm init exception = $e")
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataStore.data
                    .map { it[roundedCornerAnimateKey] }
                    .collect(object : FlowCollector<Boolean?> {
                        override suspend fun emit(value: Boolean?) {
                            Log.d("[DEBUG]", "roundedCornerAnimate changed = $value")
                            roundedCornerAnimate = value ?: false
                        }
                    })
            } catch (e: Exception) {
                Log.d("[DEBUG]", "vm init exception = $e")
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataStore.data
                    .map { it[finishAfterScannedKey] }
                    .collect(object : FlowCollector<Boolean?> {
                        override suspend fun emit(value: Boolean?) {
                            Log.d("[DEBUG]", "finishAfterScanned changed = $value")
                            finishAfterScanned = value ?: true
                        }
                    })
            } catch (e: Exception) {
                Log.d("[DEBUG]", "vm init exception = $e")
            }
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
                ?: false // default value is true, and first time will be null, so return true when null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}