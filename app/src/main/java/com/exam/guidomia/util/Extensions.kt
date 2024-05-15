package com.exam.guidomia.util

import android.annotation.SuppressLint
import android.content.res.Resources
import android.icu.number.Notation
import android.icu.number.NumberFormatter.with
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("DefaultLocale")
        /**
         * Converts Number to appropriate rounded off format with max 1 decimal place
         * **Source** [StackOverflow](https://stackoverflow.com/questions/41859525/how-to-go-about-formatting-1200-to-1-2k-in-android-studio)
         */
fun shortenPrice(number: Long) = with()
    .notation(Notation.compactShort())
    // .decimal(DecimalSeparatorDisplay.ALWAYS)
    // .precision(Precision.fixedFraction(1))
    .locale(Resources.getSystem().configuration.locales.get(0))
    .format(number)
    .toString()

fun formatPrice(price: Int): String? {
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.setMaximumFractionDigits(0)
    format.currency = Currency.getInstance("USD")
    return format.format(price)
}
