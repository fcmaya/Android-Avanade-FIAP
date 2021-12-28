package br.com.equipequatro.traveltips.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun converterBitmapToBase64(bitmap: Bitmap): String {

    val bitmapArray = ByteArrayOutputStream()

    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bitmapArray)

    return Base64.encodeToString(bitmapArray.toByteArray(), Base64.DEFAULT)
}

fun convertBase64ToBitmap(base64Image: String): Bitmap {

    val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}