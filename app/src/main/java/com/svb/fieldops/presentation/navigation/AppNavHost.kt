package com.svb.fieldops.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.svb.fieldops.presentation.screens.ClockInPreviewScreen
import com.svb.fieldops.presentation.screens.FlowCompleteScreen
import com.svb.fieldops.presentation.screens.GeofenceVerifiedScreen
import com.svb.fieldops.presentation.screens.LoginScreen
import com.svb.fieldops.presentation.screens.SelfieCaptureScreen
import com.svb.fieldops.presentation.screens.SplashScreen
import com.svb.fieldops.presentation.screens.home.DriverHomeScreen
import com.svb.fieldops.presentation.screens.home.EngineerHomeScreen
import com.svb.fieldops.presentation.screens.home.OperatorHomeScreen
import com.svb.fieldops.presentation.screens.home.SupervisorHomeScreen
import com.svb.fieldops.presentation.screens.diesel.EngineerDieselScreen
import com.svb.fieldops.presentation.screens.fuel.HsdFuelScreen
import com.svb.fieldops.presentation.screens.loadings.OperatorLoadingsScreen
import com.svb.fieldops.presentation.screens.profile.ProfileScreen
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
                        if (vm.submitLogin()) {
                            navController.navigate(ClockInRoutes.geofence)
                        }
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
                        val role = vm.uiState.value.sessionRole
                        val destination = role?.toRoute() ?: ClockInRoutes.flowComplete
                        navController.navigate(destination) {
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
        composable(MainRoutes.driver) {
            DriverHomeScreen(navController = navController)
        }
        composable(MainRoutes.operator) {
            OperatorHomeScreen(navController = navController)
        }
        composable(MainRoutes.supervisor) {
            SupervisorHomeScreen(navController = navController)
        }
        composable(MainRoutes.engineer) {
            EngineerHomeScreen(navController = navController)
        }
        composable(
            route = MainRoutes.profile,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                ProfileScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.fuelRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsHsdFuelScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                HsdFuelScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.dieselRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsDieselInventoryScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerDieselScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.loadingsRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsOperatorLoadingsScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                OperatorLoadingsScreen(role = role, navController = navController)
            }
        }
    }
}
