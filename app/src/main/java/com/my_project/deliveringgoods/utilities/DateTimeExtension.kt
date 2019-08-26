package com.my_project.deliveringgoods.utilities

import android.content.Context
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalAccessor

private const val HOURS_PATTERN = "HH:mm"
private const val FULL_HOURS_PATTERN = "HH:mm:ss"
private const val FULL_DATE_PATTERN = "dd MMM uuuu"


val String.simpleTimeFormat: String
    get() = DateTimeFormatter.ofPattern(HOURS_PATTERN).withZone(ZoneId.systemDefault()).format(ZonedDateTime.parse(this))

val String.fullTimeFormat: String
    get() = formatterFactory(FULL_HOURS_PATTERN).withZone(ZoneId.systemDefault()).format(ZonedDateTime.parse(this))

val String.fullDateFormat: String
    get() = formatterFactory(FULL_DATE_PATTERN).withZone(ZoneId.systemDefault()).format(ZonedDateTime.parse(this))

val TemporalAccessor.simpleTimeFormat: String
    get() = formatterFactory(HOURS_PATTERN).withZone(ZoneId.systemDefault()).format(this)

val TemporalAccessor.fullTimeFormat: String
    get() = formatterFactory(FULL_HOURS_PATTERN).withZone(ZoneId.systemDefault()).format(this)

val TemporalAccessor.fullDateFormat: String
    get() = formatterFactory(FULL_DATE_PATTERN).withZone(ZoneId.systemDefault()).format(this)

fun String.dateForServer(date: OffsetDateTime): String = date.toString().removeRange(19, 23)

val String.getMillis: Long
    get() = OffsetDateTime.parse(this).toEpochSecond()

val Context.getCurrentTime: Long
    get() = System.currentTimeMillis() / 1000

private fun formatterFactory(pattern: String): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

