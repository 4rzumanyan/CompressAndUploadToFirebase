package com.test.digitec.presentation.view

import androidx.compose.runtime.Immutable

@Immutable
sealed class MainSideEffects {

    object NavigateToCompressedItemsScreen : MainSideEffects()
    object NavigateToSuccessScreen : MainSideEffects()
    object NavigateToHomeScreen : MainSideEffects()
}