package com.test.picker.constant

data class AssetPickerConfig(
    val maxAssets: Int = 5,
    val gridCount: Int = 3,
    val requestType: RequestType = RequestType.COMMON,
)