package com.test.digitec.presentation.view

import androidx.compose.runtime.Immutable
import com.test.digitec.presentation.model.MediaData

@Immutable
data class MainViewState(
    val isLoading: Boolean,
    val selectedItemsList: List<MediaData>,
    val compressedItems: List<MediaData>,
    val compressedItemsPathList: List<String>
) {
    companion object {
        fun initial() = MainViewState(
            isLoading = false,
            selectedItemsList = listOf(),
            compressedItems = listOf(),
            compressedItemsPathList = listOf()
        )
    }
}