package com.tonynowater.qr_scanner_to_sms.utils

object TWCovid19SmsFormat {

    private const val KEY_PLACE_CODE = "場所代碼"
    private const val KEY_SMS_TO = "1922"

    //smsto:1922:場所代碼：111121314151617 本次實聯簡訊限防疫目的使用。
    fun isValid(input: String): Boolean {
        return input.contains(KEY_PLACE_CODE) && input.contains(KEY_SMS_TO)
    }

    fun getBody(input: String): String {
        return input.substring(input.indexOf(KEY_PLACE_CODE), input.length)
    }
}