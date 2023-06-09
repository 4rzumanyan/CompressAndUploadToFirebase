package com.test.video.data

import com.test.picker.data.AssetInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AssetInfoTest {
    private var assetInfo = AssetInfo(
        id = 8L,
        uriString = "http://lcoalhost/5",
        filename = "test.jpeg",
        directory = "Picture",
        mediaType = 1,
        size = 1150260,
        mimeType = "img/jpeg",
        duration = 16000,
        date = 23423434433
    )

    @Test
    fun `should return true when call isVideo given media type 3`() {
        val info = assetInfo.copy(mediaType = 3)

        val result = info.isVideo()

        assertTrue(result)
    }

    @Test
    fun `should return true when call isImage given media type 3`() {
        val info = assetInfo.copy(mediaType = 1)

        val result = info.isImage()

        assertTrue(result)
    }

    @Test
    fun `should return empty string when format duration given null value`() {
        val info = assetInfo.copy(duration = null)

        val result = info.formatDuration()

        assertEquals("", result)
    }

    @Test
    fun `should return minute-second string when format duration given not null value`() {
        val info = assetInfo.copy(duration = 169098)

        val result = info.formatDuration()

        assertEquals("02:49", result)
    }
}