package com.tonynowater.qr_scanner_to_sms.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.tonynowater.qr_scanner_to_sms.SettingsPreference
import com.tonynowater.qr_scanner_to_sms.model.SettingsPreferenceSerializer

val Context.dataStorePref: DataStore<Preferences> by preferencesDataStore(name = "settingPrefs")
val Context.dataStoreProtobuf: DataStore<SettingsPreference> by dataStore(
    fileName = "settings_preference.proto",
    serializer = SettingsPreferenceSerializer
)