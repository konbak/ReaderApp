package com.example.readerapp.utils

import android.icu.text.DateFormat
import com.google.firebase.Timestamp

fun formatDate(timestamp: Timestamp): String {
    val date = DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString()
        .split(",")[0]
    return date
}