package com.test.digitec.presentation.model

import android.net.Uri
import androidx.core.net.toUri
import com.test.digitec.core.enum.MediaType

data class MediaData(
    val uri: Uri,
    val size: Long,
    val path: String,
    val filename: String,
    val mimeType: String,
    val mediaType: MediaType,
    val resolution: Pair<Int, Int>
) {
    companion object {
        val DEFAULT = MediaData(
            uri = "".toUri(),
            size = 0,
            path = "",
            filename = "",
            mimeType = "",
            mediaType = MediaType.Other,
            resolution = Pair(0, 0)
        )
    }
}