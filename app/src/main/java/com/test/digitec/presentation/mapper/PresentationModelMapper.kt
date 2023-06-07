package com.test.digitec.presentation.mapper

import android.content.Context
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
import androidx.core.net.toUri
import com.test.digitec.core.enum.MediaType
import com.test.digitec.core.mapper.Mapper
import com.test.digitec.core.utils.FilePath.copyToFileAndGetPath
import com.test.digitec.core.utils.decodeToBitmap
import com.test.digitec.core.utils.getImageResolution
import com.test.digitec.core.utils.getVideoResolution
import com.test.digitec.presentation.model.MediaData
import com.test.picker.data.AssetInfo

class PresentationModelMapper(private val context: Context) : Mapper<AssetInfo, MediaData> {
    override fun map(inputModel: AssetInfo): MediaData {

        val uri = inputModel.uriString.toUri()
        val path = uri.copyToFileAndGetPath(context)
        val mediaType = when (inputModel.mediaType) {
            MEDIA_TYPE_IMAGE -> MediaType.Image
            MEDIA_TYPE_VIDEO -> MediaType.Video
            else -> MediaType.Other
        }
        val resolution = when (mediaType) {
            MediaType.Image -> uri.decodeToBitmap(context).getImageResolution()
            MediaType.Video -> path.getVideoResolution()
            MediaType.Other -> MediaData.DEFAULT.resolution
        }

        return MediaData(
            uri = uri,
            size = inputModel.size,
            path = path,
            filename = inputModel.filename,
            mimeType = inputModel.mimeType,
            mediaType = mediaType,
            resolution = resolution
        )
    }
}