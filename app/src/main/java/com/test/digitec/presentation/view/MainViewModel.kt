package com.test.digitec.presentation.view

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.test.digitec.core.enum.MediaType
import com.test.digitec.core.enum.Progress
import com.test.digitec.core.utils.clearAppCache
import com.test.digitec.core.utils.compressImages
import com.test.digitec.core.utils.compressVideos
import com.test.digitec.domain.usecase.SortUseCase
import com.test.digitec.presentation.mapper.PresentationModelMapper
import com.test.picker.data.AssetInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.File


class MainViewModel(
    private val context: Application,
    private val mapper: PresentationModelMapper,
    private val sortUseCase: SortUseCase
) : AndroidViewModel(context), ContainerHost<MainViewState, MainSideEffects> {

    companion object {
        private const val IMAGE_SIZE_KB = 1500
        private const val VIDEO_WIDTH = 1280.0
        private const val VIDEO_HEIGHT = 720.0
    }

    override val container: Container<MainViewState, MainSideEffects> =
        container(MainViewState.initial())

    fun addSelectedItemsToList(items: List<AssetInfo>) = intent {
        val selectedItemList = items.map { item -> mapper.map(item) }

        reduce {
            state.copy(
                selectedItemsList = selectedItemList,
                compressedItems = selectedItemList
            )
        }
    }

    fun clearSelectedItemsList() = intent {
        reduce { state.copy(selectedItemsList = MainViewState.initial().selectedItemsList) }
    }

    fun addCompressedItemsToList(items: List<String>) = intent {
        val compressedItemsPathList = state.compressedItemsPathList + items

        viewModelScope.launch(Dispatchers.IO) {
            val sortList = async {
                sortUseCase.sortCompressedItems(
                    compressedItemsPathList,
                    state.selectedItemsList
                )
            }
            val compressedItems = sortList.await()
            reduce {
                state.copy(
                    compressedItems = compressedItems,
                    compressedItemsPathList = compressedItemsPathList
                )
            }
        }
    }

    fun compressSelectedMedia() {

        intent {
            val selectedImagesUriList =
                state.selectedItemsList.filter { it.mediaType == MediaType.Image && it.size > 1_500_000 }
                    .map { it.uri }
            val selectedVideosUriList =
                state.selectedItemsList.filter { it.mediaType == MediaType.Video && (it.resolution.first > 1280 && it.resolution.second > 720) }
                    .map { it.uri }
            var imagesCompressionIsFinished = selectedImagesUriList.isEmpty()
            var videosCompressionIsFinished = selectedVideosUriList.isEmpty()

            if (imagesCompressionIsFinished && videosCompressionIsFinished)
                navigateToCompressedItemsScreen()
            else
                updateLoadingState(true)

            viewModelScope.launch(Dispatchers.IO) {

                launch {
                    compressImages(
                        uriList = selectedImagesUriList,
                        sizeInKb = IMAGE_SIZE_KB,
                        context = context,
                        progress = { progress: Progress, items: List<String> ->
                            if (progress == Progress.SUCCESS) {
                                addCompressedItemsToList(items)
                                imagesCompressionIsFinished = true
                                if (videosCompressionIsFinished) {
                                    updateLoadingState(false)
                                    navigateToCompressedItemsScreen()
                                }
                            }
                        })
                }

                launch {
                    compressVideos(
                        uriList = selectedVideosUriList,
                        width = VIDEO_WIDTH,
                        height = VIDEO_HEIGHT,
                        context = context,
                        progress = { progress, items ->
                            if (progress == Progress.SUCCESS) {
                                addCompressedItemsToList(items)
                                videosCompressionIsFinished = true
                                if (imagesCompressionIsFinished) {
                                    updateLoadingState(false)
                                    navigateToCompressedItemsScreen()
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    fun uploadCompressedItemsToFirebase() {
        intent {
            updateLoadingState(true)

            viewModelScope.launch(Dispatchers.IO) {
                var count = 0

                state.compressedItems.forEach {
                    launch {
                        uploadImage(it.uri, it.filename) {
                            count++
                            if (count == state.compressedItems.size) {
                                updateLoadingState(false)
                                navigateToSuccessScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun uploadImage(image: Uri, filename: String, isFinished: () -> Unit) {

        val storageReference = FirebaseStorage.getInstance().getReference(filename)

        storageReference.putFile(image)
            .addOnSuccessListener {
                isFinished.invoke()
            }
            .addOnFailureListener {
                isFinished.invoke()
            }
    }

    fun updateLoadingState(isLoading: Boolean) = intent {
        reduce { state.copy(isLoading = isLoading) }
    }

    fun navigateToCompressedItemsScreen() = intent {
        postSideEffect(MainSideEffects.NavigateToCompressedItemsScreen)
    }

    fun navigateToSuccessScreen() = intent {
        postSideEffect(MainSideEffects.NavigateToSuccessScreen)
    }

    fun navigateToHomeScreen() = intent {
        postSideEffect(MainSideEffects.NavigateToHomeScreen)
    }

    fun clearCache() {
        clearMediaCache()
        clearAppCache(context)
    }

    private fun clearMediaCache() {
        intent {
            state.compressedItems.forEach { item ->
                File(item.path).delete()
            }
        }
    }
}
