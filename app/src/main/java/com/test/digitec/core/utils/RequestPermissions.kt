package com.test.digitec.core.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat

private val permissions = arrayOf(
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
    else {
        Manifest.permission.READ_EXTERNAL_STORAGE
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    },
    Manifest.permission.CAMERA
)

@Composable
fun RequestPermissions() {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->

            for (entry in grantedMap.entries) {
                when (entry.key) {
                    Manifest.permission.READ_MEDIA_IMAGES -> {

                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {

                    }
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {

                    }
                    Manifest.permission.CAMERA -> {

                    }
                }
            }
        }

    LaunchedEffect(key1 = true) {
        launcher.launch(
            permissions
        )
    }
}

fun checkSelfPermissions(context: Context): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
    }
    return true
}