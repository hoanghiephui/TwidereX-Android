/*
 *  Twitter
 *
 *  Copyright (C) 2021-2022 Living Solutions <living.solutions.vn@gmail.com>
 * 
 *  This file is part of Twitter.
 * 
 *  Twitter is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  Twitter is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Twitter. If not, see <http://www.gnu.org/licenses/>.
 */
package com.twidere.services.utils

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import java.util.Locale

object DateFormatUtils {

    private val dateFormats = arrayOf(
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "EEE MMM dd HH:mm:ss Z yyyy",
        "MMM d, yyyy H:mm aaa ZZZZ",
        "MMM d, yyyy·H:mm aaa ZZZZ",
        "MMM d, yyyy · H:mm aaa ZZZZ",
    )

    private val formatter by lazy {
        DateTimeFormatterBuilder()
            .append(
                null,
                Array(dateFormats.size) {
                    DateTimeFormat.forPattern(dateFormats[it]).withLocale(Locale.ENGLISH).parser
                }
            )
            .toFormatter()
            .withLocale(Locale.ENGLISH)
            .withZoneUTC()
    }

    fun parse(text: String): DateTime {
        return DateTime.parse(text, formatter)
    }

    fun format(date: DateTime): String {
        return date.toString()
    }
}
