package com.tonynowater.qr_scanner_to_sms.model

import androidx.compose.ui.graphics.Color
import com.google.mlkit.vision.barcode.Barcode

data class QRCodeModel(
    @Barcode.BarcodeValueType val type: Int,
    val rawValue: String
) {
    fun getTypeDisplayName(): String = when (type) {
        Barcode.TYPE_SMS -> "簡訊"
        Barcode.TYPE_DRIVER_LICENSE -> "駕照"
        Barcode.TYPE_CALENDAR_EVENT -> "日曆"
        Barcode.TYPE_GEO -> "GPS座標"
        Barcode.TYPE_WIFI -> "Wifi資訊"
        Barcode.TYPE_URL -> "網址"
        Barcode.TYPE_CONTACT_INFO -> "聯絡人"
        Barcode.TYPE_TEXT -> "文字"
        Barcode.TYPE_PHONE -> "電話"
        else -> "其它"
    }

    fun getTypeDisplayColor(): Color = when (type) {
        Barcode.TYPE_GEO -> Color.Blue.copy(alpha = 0.5F)
        Barcode.TYPE_CONTACT_INFO -> Color.Blue.copy(alpha = 0.5F)
        Barcode.TYPE_WIFI -> Color.Green.copy(alpha = 0.4F)
        Barcode.TYPE_URL -> Color.Green.copy(alpha = 0.4F)
        Barcode.TYPE_CALENDAR_EVENT -> Color.Green.copy(alpha = 0.4F)
        Barcode.TYPE_TEXT -> Color.Black.copy(alpha = 0.25F)
        Barcode.TYPE_PHONE -> Color.Black.copy(alpha = 0.25F)
        Barcode.TYPE_SMS -> Color.Black.copy(alpha = 0.25F)
        Barcode.TYPE_DRIVER_LICENSE -> Color.Black.copy(alpha = 0.25F)
        else -> Color.Black.copy(alpha = 0.25F)
    }
}
