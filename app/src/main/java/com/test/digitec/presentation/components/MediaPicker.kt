package com.test.digitec.presentation.components

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import com.test.picker.constant.AssetPickerConfig
import com.test.picker.data.AssetInfo
import com.test.picker.data.PickerPermissions
import com.test.picker.view.AssetPicker

@Composable
fun MediaPicker(
    onPicked: (List<AssetInfo>) -> Unit,
    onClose: (List<AssetInfo>) -> Unit,
) {
    PickerPermissions(
        permissions = listOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
            else Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
        )
    ) {
        AssetPicker(
            assetPickerConfig = AssetPickerConfig(gridCount = 3),
            onPicked = onPicked,
            onClose = onClose
        )
    }
}