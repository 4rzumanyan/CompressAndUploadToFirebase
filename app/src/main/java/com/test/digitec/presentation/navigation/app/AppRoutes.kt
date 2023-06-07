package com.test.digitec.presentation.navigation.app

sealed class AppRoutes(val route: String) {
    object Home: AppRoutes(route = "home")
    object MediaPicker: AppRoutes(route = "media_picker")
    object SelectedItems: AppRoutes(route = "selected_items")
    object CompressedItems: AppRoutes(route = "compressed_items")
    object Success: AppRoutes(route = "success")
}