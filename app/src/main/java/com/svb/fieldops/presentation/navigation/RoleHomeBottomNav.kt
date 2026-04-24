package com.svb.fieldops.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.FactCheck
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.navigation.NavHostController
import com.svb.fieldops.domain.model.UserRole
import com.svb.fieldops.presentation.screens.home.HomeNavEntry

fun roleHomeRoute(role: UserRole): String = role.toRoute()

fun bottomNavItemsForRole(role: UserRole): List<HomeNavEntry> = when (role) {
    UserRole.Driver -> listOf(
        HomeNavEntry("Home", Icons.Outlined.Home),
        HomeNavEntry("Trips", Icons.Outlined.Route),
        HomeNavEntry("Fuel", Icons.Outlined.LocalGasStation),
        HomeNavEntry("Profile", Icons.Outlined.Person),
    )
    UserRole.Operator -> listOf(
        HomeNavEntry("Home", Icons.Outlined.Home),
        HomeNavEntry("Loadings", Icons.Outlined.Inventory2),
        HomeNavEntry("Fuel", Icons.Outlined.LocalGasStation),
        HomeNavEntry("Profile", Icons.Outlined.Person),
    )
    UserRole.Supervisor -> listOf(
        HomeNavEntry("Home", Icons.Outlined.Home),
        HomeNavEntry("Verify", Icons.AutoMirrored.Outlined.FactCheck),
        HomeNavEntry("Reports", Icons.Outlined.InsertChart),
        HomeNavEntry("Fuel", Icons.Outlined.LocalGasStation),
        HomeNavEntry("Profile", Icons.Outlined.Person),
    )
    UserRole.Engineer -> listOf(
        HomeNavEntry("Home", Icons.Outlined.Home),
        HomeNavEntry("Approvals", Icons.Outlined.TaskAlt),
        HomeNavEntry("Diesel", Icons.Outlined.LocalGasStation),
        HomeNavEntry("DPR", Icons.AutoMirrored.Outlined.Assignment),
        HomeNavEntry("Profile", Icons.Outlined.Person),
    )
}

fun profileTabIndex(role: UserRole): Int = bottomNavItemsForRole(role).lastIndex

/** Bottom-nav index of HSD/Fuel for roles that share [HsdFuelScreen]; null for Engineer (Diesel is separate). */
fun fuelTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Driver, UserRole.Operator -> 2
    UserRole.Supervisor -> 3
    UserRole.Engineer -> null
}

/** Bottom-nav index of Diesel for Engineer [EngineerDieselScreen]; null for other roles. */
fun dieselTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Engineer -> 2
    else -> null
}

/** Bottom-nav index of Loadings for Operator [OperatorLoadingsScreen]; null for other roles. */
fun loadingsTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Operator -> 1
    else -> null
}

/** Bottom-nav index of Trips for Driver [DriverTodaysTripsScreen]; null for other roles. */
fun tripsTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Driver -> 1
    else -> null
}

/** Bottom-nav index of Verify for Supervisor [SupervisorVerifyStartDutyScreen]; null for other roles. */
fun verifyTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Supervisor -> 1
    else -> null
}

/** Bottom-nav index of Reports for Supervisor [SupervisorReportsScreen]; null for other roles. */
fun reportsTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Supervisor -> 2
    else -> null
}

/** Bottom-nav index of Approvals for Engineer [EngineerApprovalsScreen]; null for other roles. */
fun approvalsTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Engineer -> 1
    else -> null
}

/** Bottom-nav index of DPR for Engineer [EngineerDprScreen]; null for other roles. */
fun dprTabIndex(role: UserRole): Int? = when (role) {
    UserRole.Engineer -> 3
    else -> null
}

/**
 * Bottom bar on [com.svb.fieldops.presentation.screens.breakdowns.EngineerOpenBreakdownsScreen] and
 * [com.svb.fieldops.presentation.screens.breakdowns.EngineerCloseBreakdownScreen] (Engineer + Supervisor).
 */
fun NavHostController.onBreakdownFlowBottomNavSelect(role: UserRole, index: Int) {
    val homeIdx = 0
    val profileIdx = profileTabIndex(role)
    when (role) {
        UserRole.Engineer -> {
            val approvalsIdx = requireNotNull(approvalsTabIndex(role))
            val dieselIdx = requireNotNull(dieselTabIndex(role))
            val dprIdx = requireNotNull(dprTabIndex(role))
            when {
                index == homeIdx -> popRoleHomeWithHomeTabSelected()
                index == profileIdx ->
                    navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                index == approvalsIdx ->
                    navigate(MainRoutes.approvals(role)) { launchSingleTop = true }
                index == dieselIdx ->
                    navigate(MainRoutes.diesel(role)) { launchSingleTop = true }
                index == dprIdx ->
                    navigate(MainRoutes.dpr(role)) { launchSingleTop = true }
                else -> popRoleHomeWithHomeTabSelected()
            }
        }
        UserRole.Supervisor -> {
            val verifyIdx = requireNotNull(verifyTabIndex(role))
            val reportsIdx = requireNotNull(reportsTabIndex(role))
            val fuelIdx = requireNotNull(fuelTabIndex(role))
            when {
                index == homeIdx -> popRoleHomeWithHomeTabSelected()
                index == profileIdx ->
                    navigate(MainRoutes.profile(role)) { launchSingleTop = true }
                index == verifyIdx ->
                    navigate(MainRoutes.verifyStartDuty(role)) { launchSingleTop = true }
                index == reportsIdx ->
                    navigate(MainRoutes.reports(role)) { launchSingleTop = true }
                index == fuelIdx ->
                    navigate(MainRoutes.fuel(role)) { launchSingleTop = true }
                else -> popRoleHomeWithHomeTabSelected()
            }
        }
        else -> popRoleHomeWithHomeTabSelected()
    }
}
