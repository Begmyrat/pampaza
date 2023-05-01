package com.fabrika.pampaza.utils.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long?.toDateString(format: String = "dd/MM/yyyy"): String {
    if(this == null) return ""
    return SimpleDateFormat(format).format(
            Date(this)
        )
}