package com.tonynowater.qr_scanner_to_sms.model

import android.content.*
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.google.mlkit.vision.barcode.Barcode
import com.tonynowater.qr_scanner_to_sms.ui.theme.JOY_GREEN

data class QRCodeModel(
    @Barcode.BarcodeValueType val type: Int,
    val rawValue: String,
    val barcode: Barcode
) {
    fun action(context: Context) {
        try {
            when (type) {
                Barcode.TYPE_URL -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(rawValue)))
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
                else -> {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboardManager.setPrimaryClip(
                        ClipData.newPlainText(
                            "bar code value",
                            rawValue
                        )
                    )
                }
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "?????????????????? ${getTypeDisplayName()}($rawValue)", Toast.LENGTH_SHORT).show()
        }
    }

    fun actionText(): String = when (type) {
        Barcode.TYPE_URL -> "????????????"
        Barcode.TYPE_SMS -> "????????????"
        Barcode.TYPE_PHONE -> "????????????"
        Barcode.TYPE_CONTACT_INFO -> "???????????????"
        else -> "????????????"
    }

    fun getTypeDisplayName(): String = when (type) {
        Barcode.TYPE_SMS -> "??????"
        Barcode.TYPE_DRIVER_LICENSE -> "??????"
        Barcode.TYPE_CALENDAR_EVENT -> "??????"
        Barcode.TYPE_GEO -> "GPS??????"
        Barcode.TYPE_WIFI -> "Wifi??????"
        Barcode.TYPE_URL -> "??????"
        Barcode.TYPE_CONTACT_INFO -> "?????????"
        Barcode.TYPE_TEXT -> "??????"
        Barcode.TYPE_PHONE -> "??????"
        Barcode.TYPE_PRODUCT -> "????????????"
        else -> "??????"
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
