package com.example.paymeinternapp.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {

    fun formatTimer(time: Long): String {
        return SimpleDateFormat("mm:ss:SS", Locale.getDefault()).format(Date(time))
    }
}

fun Offset.toIntOffset() = IntOffset(x.toInt(), y.toInt())