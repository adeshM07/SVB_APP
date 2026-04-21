package com.svb.fieldops.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.svb.fieldops.presentation.screens.ClockInPreviewScreen
import com.svb.fieldops.presentation.screens.FlowCompleteScreen
import com.svb.fieldops.presentation.screens.GeofenceVerifiedScreen
import com.svb.fieldops.presentation.screens.LoginScreen
import com.svb.fieldops.presentation.screens.SelfieCaptureScreen
import com.svb.fieldops.presentation.screens.SplashScreen
import com.svb.fieldops.presentation.viewmodel.ClockInFlowViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = ClockInRoutes.graph,
    ) {
        navigation(
            route = ClockInRoutes.graph,
            startDestination = ClockInRoutes.splash,
        ) {
            composable(ClockInRoutes.splash) {
                SplashScreen(
                    onProfileClick = { /* later: profile / settings */ },
                    onGetStarted = { navController.navigate(ClockInRoutes.login) },
                )
            }
            composable(ClockInRoutes.login) { entry ->
                val graphEntry = remember(entry) {
                    navController.getBackStackEntry(ClockInRoutes.graph)
                }
                val vm: ClockInFlowViewModel = hiltViewModel(graphEntry)
                val state by vm.uiState.collectAsStateWithLifecycle()
                LoginScreen(
                    state = state,
                    onEmployeeIdChange = vm::onEmployeeIdChange,
                    onUniqueCodeChange = vm::onUniqueCodeChange,
                    onLogin = {
                        vm.onLoginSubmitted()
                        navController.navigate(ClockInRoutes.geofence)
                    },
                )
            }
            composable(ClockInRoutes.geofence) { entry ->
                val graphEntry = remember(entry) {
                    navController.getBackStackEntry(ClockInRoutes.graph)
                }
                val vm: ClockInFlowViewModel = hiltViewModel(graphEntry)
                val state by vm.uiState.collectAsStateWithLifecycle()
                GeofenceVerifiedScreen(
                    state = state,
                    onContinue = { navController.navigate(ClockInRoutes.selfie) },
                )
            }
            composable(ClockInRoutes.selfie) { entry ->
                val graphEntry = remember(entry) {
                    navController.getBackStackEntry(ClockInRoutes.graph)
                }
                val vm: ClockInFlowViewModel = hiltViewModel(graphEntry)
                SelfieCaptureScreen(
                    onBack = { navController.popBackStack() },
                    onSimulateCapture = {
                        vm.onSelfiePlaceholderCaptured()
                        navController.navigate(ClockInRoutes.preview)
                    },
                )
            }
            composable(ClockInRoutes.preview) { entry ->
                val graphEntry = remember(entry) {
                    navController.getBackStackEntry(ClockInRoutes.graph)
                }
                val vm: ClockInFlowViewModel = hiltViewModel(graphEntry)
                val state by vm.uiState.collectAsStateWithLifecycle()
                ClockInPreviewScreen(
                    state = state,
                    onRetake = {
                        vm.clearSelfie()
                        navController.popBackStack()
                    },
                    onClockIn = {
                        navController.navigate(ClockInRoutes.flowComplete) {
                            popUpTo(ClockInRoutes.graph) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
        composable(ClockInRoutes.flowComplete) {
            FlowCompleteScreen()
        }
    }
}
