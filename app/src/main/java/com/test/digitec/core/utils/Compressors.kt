package com.test.digitec.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.test.digitec.R
import com.test.digitec.core.enum.Progress
import com.test.digitec.core.utils.FilePath.copyToFileAndGetIt
import com.test.video.CompressionListener
import com.test.video.VideoCompressor
import com.test.video.config.Configuration
import com.test.video.config.StorageConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

suspend fun compressImages(
    uriList: List<Uri>,
    sizeInKb: Int,
    context: Context,
    progress: (Progress, List<String>) -> Unit
) {
    val pathList: MutableList<String> = mutableListOf()

    uriList.forEachIndexed { index, uri ->
        var compressedFile = uri.copyToFileAndGetIt(context)
        var quality = 100

        while (compressedFile.sizeInKb() > sizeInKb) {
            compressedFile.delete()
            compressedFile = uri.copyToFileAndGetIt(context)
            compressedFile = compressedFile.compress(quality)!!
            quality -= 2
        }
        pathList.add(compressedFile.path)
    }
    progress.invoke(Progress.SUCCESS, pathList)
}

private suspend fun File.compress(quality: Int): File? {
    var result: File?

    withContext(Dispatchers.IO) {
        try {
            val o = BitmapFactory.Options()
            val inputStream = FileInputStream(this@compress)
            val a = BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // here i override the original image file
            this@compress.createNewFile()

            val outputStream = FileOutputStream(this@compress)
            a!!.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            result = this@compress

        } catch (e: Exception) {
            Log.e("exception", e.message.toString())
            result = null
        }
    }

    return result
}

suspend fun compressVideos(
    uriList: List<Uri>,
    width: Double,
    height: Double,
    context: Context,
    progress: (Progress, MutableList<String>) -> Unit
) {
    VideoCompressor.start(
        context = context,
        uris = uriList,
        isStreamable = true,
        storageConfiguration = StorageConfiguration(
            isExternal = true,
        ),
        configureWith = Configuration(
            videoWidth = width,
            videoHeight = height
        ),
        listener = object : CompressionListener {
            var count = 0
            val pathList: MutableList<String> = mutableListOf()

            override fun onProgress(index: Int, percent: Float) {
                // Update UI with progress value
            }

            override fun onStart(index: Int) {
                // Compression start
            }

            override fun onSuccess(index: Int, size: Long, path: String?) {
                count++
                pathList.add(path.toString())

                if (count == uriList.size)
                    progress.invoke(Progress.SUCCESS, pathList)
                // On Compression success
            }

            override fun onFailure(index: Int, failureMessage: String) {
                context.showShortToast(R.string.video_compression_failed)
                // On Failure
            }

            override fun onCancelled(index: Int) {
                // On Cancelled
            }
        }
    )
}