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
    }

    private val dataStore = app.dataStore

    var vibration: Boolean by mutableStateOf(false)
        private set

    init {
        Log.d("[DEBUG]", "vm init: ")
        viewModelScope.launch {
            try {
                dataStore.data.map {
                    it[vibrateKey]
                }.collect(object : FlowCollector<Boolean?> {
                    override suspend fun emit(value: Boolean?) {
                        vibration = value ?: true
                    }
                })
            } catch (e: Exception) {
                Log.d("[DEBUG]", "vm init exception = $e")
            }
        }
    }

    suspend fun updateVibrate() {
        dataStore.edit {
            it[vibrateKey] = it[vibrateKey]?.not() ?: false // first will be null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

}