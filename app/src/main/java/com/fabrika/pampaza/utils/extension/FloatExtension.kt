package com.fabrika.pampaza.utils.extension

import android.content.Context

fun Int.toDP(context: Context): Int {
    val screenPixelDensity = context.resources.displayMetrics.density
    return (this * screenPixelDensity).toInt()
}