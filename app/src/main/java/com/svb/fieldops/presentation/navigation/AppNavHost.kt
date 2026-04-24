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
import com.svb.fieldops.presentation.screens.fuel.SupervisorVerifyHsdRequestFlowScreen
import com.svb.fieldops.presentation.screens.fuel.SupervisorVerifyHsdRequestsScreen
import com.svb.fieldops.presentation.screens.loadings.OperatorLoadingsScreen
import com.svb.fieldops.presentation.screens.trips.DriverTodaysTripsScreen
import com.svb.fieldops.presentation.screens.verify.SupervisorVerifyStartDutyScreen
import com.svb.fieldops.presentation.screens.verify.SupervisorVerifyTripScreen
import com.svb.fieldops.presentation.screens.profile.ProfileScreen
import com.svb.fieldops.presentation.screens.approvals.EngineerApprovalsScreen
import com.svb.fieldops.presentation.screens.dpr.EngineerDprScreen
import com.svb.fieldops.presentation.screens.breakdowns.EngineerCloseBreakdownScreen
import com.svb.fieldops.presentation.screens.breakdowns.EngineerOpenBreakdownsScreen
import com.svb.fieldops.presentation.screens.breakdowns.SupervisorReportBreakdownScreen
import com.svb.fieldops.presentation.screens.endjob.EngineerEndJobSiteScreen
import com.svb.fieldops.presentation.screens.sitestart.EngineerVerifySiteStartScreen
import com.svb.fieldops.presentation.screens.zones.EngineerZoneWorkPlanScreen
import com.svb.fieldops.presentation.screens.reports.SupervisorReportsScreen
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
        composable(
            route = MainRoutes.tripsRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsDriverTripsScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                DriverTodaysTripsScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.verifyStartDutyRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsSupervisorVerifyStartDutyScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                SupervisorVerifyStartDutyScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.verifyTripRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsSupervisorVerifyTripScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                SupervisorVerifyTripScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.reportBreakdownRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsSupervisorReportBreakdownScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                SupervisorReportBreakdownScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.verifyHsdRequestsRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsSupervisorVerifyHsdRequestsScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                SupervisorVerifyHsdRequestsScreen(
                    role = role,
                    navController = navController,
                    listSavedStateHandle = entry.savedStateHandle,
                )
            }
        }
        composable(
            route = MainRoutes.verifyHsdRequestFlowRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
                navArgument("requestId") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            val requestId = entry.arguments?.getString("requestId").orEmpty()
            if (role == null || !role.supportsSupervisorVerifyHsdRequestFlowScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                SupervisorVerifyHsdRequestFlowScreen(
                    role = role,
                    requestId = requestId,
                    navController = navController,
                )
            }
        }
        composable(
            route = MainRoutes.reportsRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsSupervisorReportsScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                SupervisorReportsScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.approvalsRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsEngineerApprovalsScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerApprovalsScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.dprRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsEngineerDprScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerDprScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.zoneWorkPlanRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsEngineerZoneWorkPlanScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerZoneWorkPlanScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.verifySiteStartRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsEngineerVerifySiteStartScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerVerifySiteStartScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.endJobSiteRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsEngineerEndJobSiteScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerEndJobSiteScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.openBreakdownsRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
            ),
        ) { entry ->
            val raw = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(raw)
            if (role == null || !role.supportsEngineerOpenBreakdownsScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerOpenBreakdownsScreen(role = role, navController = navController)
            }
        }
        composable(
            route = MainRoutes.closeBreakdownRoute,
            arguments = listOf(
                navArgument("role") { type = NavType.StringType },
                navArgument("breakdownId") { type = NavType.StringType },
            ),
        ) { entry ->
            val rawRole = entry.arguments?.getString("role")
            val role = parseUserRoleFromArg(rawRole)
            val breakdownId = entry.arguments?.getString("breakdownId").orEmpty()
            if (role == null || !role.supportsEngineerCloseBreakdownScreen()) {
                LaunchedEffect(Unit) { navController.popBackStack() }
            } else {
                EngineerCloseBreakdownScreen(
                    role = role,
                    breakdownId = breakdownId,
                    navController = navController,
                )
            }
        }
    }
}
