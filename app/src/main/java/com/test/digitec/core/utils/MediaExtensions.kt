package com.test.digitec.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.Formatter
import java.io.File

fun List<Uri>.decodeToBitmap(context: Context): List<Bitmap> {
    val bitmapList: MutableList<Bitmap> = mutableListOf()

    this.forEach { uri ->
        uri.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmapList.add(
                    MediaStore.Images
                        .Media.getBitmap(context.contentResolver, uri)
                )
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                bitmapList.add(ImageDecoder.decodeBitmap(source))
            }
        }
    }
    return bitmapList
}

fun Uri.decodeToBitmap(context: Context): Bitmap {
    val bitmap = if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images
            .Media.getBitmap(context.contentResolver, this)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, this)
        ImageDecoder.decodeBitmap(source)
    }

    return bitmap
}

fun File.sizeInB(): Long = this.length()

fun File.sizeInKb(): Long = this.length() / 1_000

fun File.sizeInmB(): Long = this.length() / 1_000_000

fun Long.formatSize(context: Context): String = Formatter.formatFileSize(context, this)

fun File.formatSize(context: Context): String = Formatter.formatFileSize(context, this.length())

fun Long.formatShortSize(context: Context): String =
    Formatter.formatShortFileSize(context, this)

fun File.formatShortSize(context: Context): String =
    Formatter.formatShortFileSize(context, this.length())

fun String.getVideoResolution(): Pair<Int, Int> {
    val mdr = MediaMetadataRetriever()
    mdr.setDataSource(this)
    val width: Int =
        mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
    val height: Int =
        mdr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0

    return Pair(width, height)
}

fun Bitmap.getImageResolution(): Pair<Int, Int> = Pair(this.width, this.height)

