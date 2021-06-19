package com.tonynowater.qr_scanner_to_sms.model

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.tonynowater.qr_scanner_to_sms.SettingsPreference
import java.io.InputStream
import java.io.OutputStream

object SettingsPreferenceSerializer : Serializer<SettingsPreference> {
    
    override val defaultValue: SettingsPreference
        get() = SettingsPreference.newBuilder()
            .setIsVibration(true)
            .setIsFinishingAppAfterScan(true)
            .setIsEnableAnimation(false)
            .build()

    override suspend fun readFrom(input: InputStream): SettingsPreference {
        try {
            return SettingsPreference.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SettingsPreference, output: OutputStream) {
        t.writeTo(output)
    }
}