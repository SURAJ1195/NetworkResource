package com.example.mynetworkcall

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConverterImpl: ConverterInterface {
    @SuppressLint("Recycle")
    override fun toByteArray(uri:Uri,context: Context): ByteArray? {
        val iStream = context.contentResolver.openInputStream(uri)
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len: Int
        return if (iStream != null) {
            while (iStream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            byteBuffer.toByteArray()
        } else {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun toBase64(byteArray:ByteArray?): String? = try {
        String(java.util.Base64.getEncoder().encode(byteArray))
    } catch (e: Exception) {
        null
    }


    override fun toEncodedBase64(string: String?): String? = try {
        val bytes = string?.toByteArray(Charsets.UTF_8)
        Base64.encodeToString(bytes, Base64.DEFAULT)

    } catch (e: Exception) {
        null
    }


    override fun toDecodedBase64(string:String?): String? =try {
        val bytes = string.let { Base64.decode(it, Base64.DEFAULT) }
        String(bytes, Charsets.UTF_8)
    } catch (e: Exception) {
        null
    }

    override fun toBitMapFromBase64(string:String?): Bitmap? = try {
        val decodedBytes = Base64.decode(string, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }



    override fun cropCenter(bitmap:Bitmap?): Bitmap? {
        return try {
            val centerX = (bitmap?.width ?: 0) / 2
            val centerY = (bitmap?.height ?: 0) / 2
            val width = 200
            val height = 200
            val left = centerX - (width / 2)
            val top = centerY - (height / 2)
//            val right = centerX + (width / 2)
//            val bottom = centerY + (height / 2)
            val croppedBitmap = Bitmap.createBitmap(bitmap!!, left, top, width, height)
            croppedBitmap
        } catch (e: Exception) {
            null
        }
    }

    override fun toByteArray(bitmap:Bitmap?): ByteArray? {
        try {
            ByteArrayOutputStream().use { stream ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                return stream.toByteArray()
            }
        } catch (e: Exception) {
            return null
        }
    }


    override fun <T : Number> toWholeNumber(number:T): T {
        val formattedNumber = DecimalFormat("00000").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun <T : Number> toOneDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.0").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun <T : Number> toTwoDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.00").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun <T : Number> toThreeDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.000").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun <T : Number> toFourDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.0000").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun <T : Number> toFiveDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.00000").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun <T : Number> toSixDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.000000").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override  fun <T : Number> toSevenDigits(number:T): T {
        val formattedNumber = DecimalFormat("00000.0000000").format(number).toDouble()
        @Suppress("UNCHECKED_CAST")
        return when (number) {
            is Byte -> formattedNumber.toInt().toByte() as T
            is Short -> formattedNumber.toInt().toShort() as T
            is Int -> formattedNumber.toInt() as T
            is Long -> formattedNumber.toLong() as T
            is Float -> formattedNumber.toFloat() as T
            is Double -> formattedNumber as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
    override fun toTime(number:Number): String {
        val sdf = SimpleDateFormat("HH:mm:ss:SS", Locale.getDefault())
        val time = Calendar.getInstance().apply { timeInMillis = number.toLong() }
        return sdf.format(time.time)
    }
}