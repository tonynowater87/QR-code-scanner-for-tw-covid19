package com.tonynowater.qr_scanner_to_sms.model

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import androidx.compose.ui.graphics.Color
import com.google.mlkit.vision.barcode.Barcode
import com.tonynowater.qr_scanner_to_sms.ui.theme.JOY_GREEN

data class QRCodeModel(
    @Barcode.BarcodeValueType val type: Int,
    val rawValue: String,
    val barcode: Barcode
) {
    fun action(context: Context) {
        when (type) {
            Barcode.TYPE_URL -> {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(rawValue)))
            }
            Barcode.TYPE_TEXT -> {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText("bar code value", rawValue))
            }
            Barcode.TYPE_SMS -> {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("sms:${barcode.sms.phoneNumber}")
                    ).apply {
                        putExtra("sms_body", barcode.sms.message)
                    })
            }
            Barcode.TYPE_PHONE -> {
                context.startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.fromParts("tel", barcode.phone.number, null)
                    )
                )
            }
            Barcode.TYPE_CONTACT_INFO -> {
                context.startActivity(
                    Intent(ContactsContract.Intents.Insert.ACTION).apply {
                        type = ContactsContract.RawContacts.CONTENT_TYPE
                        putExtra(
                            ContactsContract.Intents.Insert.NAME,
                            barcode.contactInfo.name.formattedName ?: ""
                        )
                        putExtra(
                            ContactsContract.Intents.Insert.COMPANY,
                            barcode.contactInfo.organization ?: ""
                        )
                        putExtra(
                            ContactsContract.Intents.Insert.EMAIL,
                            barcode.contactInfo.emails.firstOrNull()?.address ?: ""
                        )
                        putExtra(
                            ContactsContract.Intents.Insert.EMAIL_TYPE,
                            barcode.contactInfo.emails.firstOrNull()?.type
                                ?: ContactsContract.CommonDataKinds.Email.TYPE_OTHER
                        )
                        putExtra(
                            ContactsContract.Intents.Insert.PHONE,
                            barcode.contactInfo.phones.firstOrNull()?.number
                                ?: ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
                        )
                        putExtra(
                            ContactsContract.Intents.Insert.PHONE_TYPE,
                            barcode.contactInfo.phones.firstOrNull()?.type
                                ?: ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
                        )
                    }
                )
            }
            else -> false
        }
    }

    fun actionText(): String = when (type) {
        Barcode.TYPE_URL -> "開啟網頁"
        Barcode.TYPE_TEXT -> "複製內容"
        Barcode.TYPE_SMS -> "開啟訊息"
        Barcode.TYPE_PHONE -> "開啟電話"
        Barcode.TYPE_CONTACT_INFO -> "開啟聯絡人"
        else -> ""
    }

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
        Barcode.TYPE_WIFI -> JOY_GREEN
        Barcode.TYPE_URL -> JOY_GREEN
        Barcode.TYPE_CALENDAR_EVENT -> JOY_GREEN
        Barcode.TYPE_TEXT -> Color.Black.copy(alpha = 0.25F)
        Barcode.TYPE_PHONE -> Color.Black.copy(alpha = 0.25F)
        Barcode.TYPE_SMS -> Color.Black.copy(alpha = 0.25F)
        Barcode.TYPE_DRIVER_LICENSE -> Color.Black.copy(alpha = 0.25F)
        else -> Color.Black.copy(alpha = 0.25F)
    }
}
