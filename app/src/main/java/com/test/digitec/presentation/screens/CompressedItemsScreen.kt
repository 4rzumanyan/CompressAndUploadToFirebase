package com.test.digitec.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.test.digitec.R
import com.test.digitec.core.utils.formatShortSize
import com.test.digitec.presentation.components.GradientButtonRound
import com.test.digitec.presentation.components.OnLifecycleEvent
import com.test.digitec.presentation.navigation.BackPressHandler
import com.test.digitec.presentation.navigation.app.AppRoutes
import com.test.digitec.presentation.view.MainSideEffects
import com.test.digitec.presentation.view.MainViewModel
import com.test.digitec.ui.theme.Mulberry
import com.test.digitec.ui.theme.Watermelon
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalTextApi::class)
@Composable
fun CompressedItemsScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val state by viewModel.collectAsState()

    val brush = Brush.horizontalGradient(listOf(Mulberry, Watermelon))

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffects.NavigateToSuccessScreen -> navController.navigate(AppRoutes.Success.route)
            else -> {}
        }
    }

    if (state.isLoading) {
        Dialog(
            onDismissRequest = { viewModel.updateLoadingState(false) },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator(color = Watermelon)
                Text(
                    text = stringResource(id = R.string.uploading),
                    style = TextStyle(brush = brush),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .weight(1f)
        ) {
            itemsIndexed(
                state.compressedItems
            ) { i, item ->
                GlideImage(
                    model = item.uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.25f)
                        .border(
                            border = BorderStroke(
                                2.dp, brush = brush
                            )
                        )
                )
                Text(
                    text = item.filename,
                    fontSize = 14.sp,
                    style = TextStyle(brush = brush),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
                Text(
                    text = stringResource(
                        id = R.string.resolution_before_after,
                        state.selectedItemsList[i].resolution.first,
                        state.selectedItemsList[i].resolution.second,
                        item.resolution.first,
                        item.resolution.second
                    ),
                    fontSize = 12.sp,
                    style = TextStyle(brush = brush),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(
                        id = R.string.size_before_after,
                        state.selectedItemsList[i].size.formatShortSize(context),
                        item.size.formatShortSize(context)
                    ),
                    fontSize = 12.sp,
                    style = TextStyle(brush = brush),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )
            }
        }
        GradientButtonRound(
            modifier = Modifier,
            enableText = R.string.upload,
            disableText = R.string.upload,
            enableColors = listOf(Mulberry, Watermelon),
            widthFraction = 0.68f,
            paddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            isStatic = true
        ) {
            viewModel.uploadCompressedItemsToFirebase()
        }
    }

    BackPressHandler {}
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> viewModel.clearCache()
            else -> {}
        }
    }
}

