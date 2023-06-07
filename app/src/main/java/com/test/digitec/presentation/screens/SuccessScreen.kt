package com.test.digitec.presentation.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.test.digitec.R
import com.test.digitec.presentation.components.GradientButtonRound
import com.test.digitec.presentation.components.OnLifecycleEvent
import com.test.digitec.presentation.navigation.BackPressHandler
import com.test.digitec.presentation.navigation.app.AppRoutes
import com.test.digitec.presentation.view.MainSideEffects
import com.test.digitec.presentation.view.MainViewModel
import com.test.digitec.ui.theme.Mulberry
import com.test.digitec.ui.theme.Watermelon
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalTextApi::class)
@Composable
fun SuccessScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val activity = (LocalContext.current as? Activity)

    val brush = Brush.horizontalGradient(listOf(Mulberry, Watermelon))

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffects.NavigateToHomeScreen -> navController.navigate(AppRoutes.Home.route)
            else -> {}
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.congratulations),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            style = TextStyle(brush = brush),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        GradientButtonRound(
            modifier = Modifier.padding(top = 200.dp),
            enableText = R.string.home,
            disableText = R.string.home,
            enableColors = listOf(Mulberry, Watermelon),
            widthFraction = 0.68f,
            paddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            isStatic = true
        ) {
            viewModel.clearCache()
            viewModel.navigateToHomeScreen()
        }

        GradientButtonRound(
            modifier = Modifier.padding(top = 16.dp),
            enableText = R.string.exit,
            disableText = R.string.exit,
            enableColors = listOf(Mulberry, Watermelon),
            widthFraction = 0.68f,
            paddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            isStatic = true
        ) {
            activity?.finish()
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