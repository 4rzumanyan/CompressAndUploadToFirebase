package com.test.digitec.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.test.digitec.R
import com.test.digitec.core.utils.RequestPermissions
import com.test.digitec.core.utils.checkSelfPermissions
import com.test.digitec.presentation.components.Dialog
import com.test.digitec.presentation.components.GradientButtonRound
import com.test.digitec.presentation.navigation.app.AppRoutes
import com.test.digitec.ui.theme.Mulberry
import com.test.digitec.ui.theme.Watermelon

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    var needsPermissions by remember { mutableStateOf(false) }

    if (!checkSelfPermissions(context))
        Dialog(
            descriptionText = R.string.grant_permission,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ) {
            needsPermissions = it
        }
    if (needsPermissions)
        RequestPermissions()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        GradientButtonRound(
            modifier = Modifier,
            enableText = R.string.select_media,
            disableText = R.string.select_media,
            enableColors = listOf(Mulberry, Watermelon),
            widthFraction = 0.68f,
            paddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            isStatic = true
        ) {
            navController.navigate(AppRoutes.MediaPicker.route)
        }
    }
}