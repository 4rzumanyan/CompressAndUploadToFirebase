package com.test.digitec.core.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FilePath {

    /**
     * The method will copy and return the path of the given uri
     *
     * @author Hrant Arzumanyan
     *
     * @param context
     */
    fun Uri.copyToFileAndGetPath(context: Context): String = copyToCache(this, context).first

    /**
     * The method will copy and return the file of the given uri
     *
     * @author Hrant Arzumanyan
     *
     * @param context
     */
    fun Uri.copyToFileAndGetIt(context: Context): File = copyToCache(this, context).second


    @SuppressLint("Recycle")
    private fun copyToCache(fileUri: Uri, context: Context): Pair<String, File> {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(fileUri, "r", null)
        val path =
            "${context.externalCacheDir?.absoluteFile}/${context.contentResolver.getFileName(fileUri)}"
        val file = File(path)

        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()
        }

        return Pair(path, file)
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)

        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }

        return name
    }
}