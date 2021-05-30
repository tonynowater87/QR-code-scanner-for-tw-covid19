package com.tonynowater.qr_scanner_to_sms.utils

import org.junit.Test

class TWCovid19SmsFormatTest {

    @Test
    fun case1() {
        val result = TWCovid19SmsFormat.isValid("smsto:1922:場所代碼：111121314151617 本次實聯簡訊限防疫目的使用。")
        assert(result)
    }

    @Test
    fun case2() {
        val input = "smsto:1922:場所代碼：111121314151617 本次實聯簡訊限防疫目的使用。"

        assert(TWCovid19SmsFormat.isValid(input))

        val result = TWCovid19SmsFormat.getBody(input)
        assert(result == "場所代碼：111121314151617 本次實聯簡訊限防疫目的使用。")
    }
}