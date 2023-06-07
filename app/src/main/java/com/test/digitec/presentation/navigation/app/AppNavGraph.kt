package com.test.digitec.presentation.navigation.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.test.digitec.presentation.components.MediaPicker
import com.test.digitec.presentation.screens.CompressedItemsScreen
import com.test.digitec.presentation.screens.HomeScreen
import com.test.digitec.presentation.screens.SelectedItemsScreen
import com.test.digitec.presentation.screens.SuccessScreen
import com.test.digitec.presentation.view.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    val viewModel = koinViewModel<MainViewModel>()

    AnimatedNavHost(navController = navController, startDestination = AppRoutes.Home.route) {
        composable(
            route = AppRoutes.Home.route
        ) {
            HomeScreen(
                navController = navController
            )
        }
        composable(
            route = AppRoutes.MediaPicker.route
        ) {
            MediaPicker(
                onPicked = { items ->
                    viewModel.clearSelectedItemsList()
                    viewModel.addSelectedItemsToList(items)
                    navController.navigate(AppRoutes.SelectedItems.route)
                },
                onClose = {
                    viewModel.clearSelectedItemsList()
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = AppRoutes.SelectedItems.route
        ) {
            SelectedItemsScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(
            route = AppRoutes.CompressedItems.route
        ) {
            CompressedItemsScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(
            route = AppRoutes.Success.route
        ) {
            SuccessScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}