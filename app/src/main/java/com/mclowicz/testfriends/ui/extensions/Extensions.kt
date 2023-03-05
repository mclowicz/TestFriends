package com.mclowicz.testfriends.ui.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateTime(): String {
    val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US)
    return dateTimeFormat.format(this)
}