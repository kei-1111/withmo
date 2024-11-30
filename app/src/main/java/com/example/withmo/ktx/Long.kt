package com.example.withmo.ktx

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toZonedDateTime(): ZonedDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
}
