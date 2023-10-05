package com.example.mynetworkcall

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

interface ConverterInterface {

    @SuppressLint("Recycle")
    fun toByteArray(uri: Uri, context: Context): ByteArray?

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64(byteArray: ByteArray?): String?
    fun toEncodedBase64(string: String?): String?
    fun toDecodedBase64(string: String?): String?
    fun toBitMapFromBase64(string: String?): Bitmap?
    fun cropCenter(bitmap: Bitmap?): Bitmap?
    fun toByteArray(bitmap: Bitmap?): ByteArray?
    fun <T : Number> toWholeNumber(number: T): T
    fun <T : Number> toOneDigits(number: T): T
    fun <T : Number> toTwoDigits(number: T): T
    fun <T : Number> toThreeDigits(number: T): T
    fun <T : Number> toFourDigits(number: T): T
    fun <T : Number> toFiveDigits(number: T): T
    fun <T : Number> toSixDigits(number: T): T
    fun <T : Number> toSevenDigits(number: T): T
    fun toTime(number: Number): String
}