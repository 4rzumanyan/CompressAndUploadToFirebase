package com.test.digitec.domain.usecase

import android.content.Context
import androidx.core.net.toUri
import com.test.digitec.core.enum.MediaType
import com.test.digitec.core.utils.decodeToBitmap
import com.test.digitec.core.utils.getImageResolution
import com.test.digitec.core.utils.getVideoResolution
import com.test.digitec.core.utils.sizeInB
import com.test.digitec.presentation.model.MediaData
import java.io.File

class SortUseCase(
    private val context: Context
) {
    suspend fun sortCompressedItems(
        newDataPathList: List<String>,
        oldData: List<MediaData>
    ): List<MediaData> {
        val result: MutableList<MediaData> = mutableListOf()
        val newData: MutableMap<String, String> = mutableMapOf()

        newDataPathList.forEach { path ->
            val filename = path.substring(path.lastIndexOf("/") + 1)
            newData[filename] = path
        }

        oldData.forEach { data ->
            val path = newData[data.filename]

            if (path != null) {
                val file = File(path)
                val updatedData = data.copy(
                    uri = file.toUri(),
                    size = file.sizeInB(),
                    path = path,
                    filename = data.filename,
                    mimeType = data.mimeType,
                    mediaType = data.mediaType,
                    resolution = when (data.mediaType) {
                        MediaType.Image -> file.toUri().decodeToBitmap(context).getImageResolution()
                        MediaType.Video -> path.getVideoResolution()
                        MediaType.Other -> MediaData.DEFAULT.resolution
                    }
                )
                result.add(updatedData)
            } else
                result.add(data)
        }

        return result
    }
}