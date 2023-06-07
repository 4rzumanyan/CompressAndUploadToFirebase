package com.test.digitec.core.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import java.io.File

fun Context.showShortToast(@StringRes msg: Int) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

private fun deleteRecursive(fileOrDirectory: File) {
    if (fileOrDirectory.isDirectory)
        fileOrDirectory.listFiles()?.forEach { file ->
            deleteRecursive(file)
        }
    fileOrDirectory.delete()
}

fun clearAppCache(context: Context) {
    val cache = context.externalCacheDir

    if (cache != null) {
        deleteRecursive(cache)
    }
}