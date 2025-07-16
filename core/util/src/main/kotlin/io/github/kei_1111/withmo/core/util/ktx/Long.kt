package io.github.kei_1111.withmo.core.util.ktx

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun Long.toZonedDateTime(): ZonedDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault())
}
