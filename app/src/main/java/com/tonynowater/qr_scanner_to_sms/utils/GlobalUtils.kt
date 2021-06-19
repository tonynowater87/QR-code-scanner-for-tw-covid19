package com.tonynowater.qr_scanner_to_sms.utils

import android.os.Looper

fun isMainThread() = Looper.myLooper()?.thread == Looper.getMainLooper().thread