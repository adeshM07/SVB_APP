package com.svb.fieldops.presentation.navigation

import com.svb.fieldops.domain.model.UserRole
import androidx.navigation.NavHostController

object MainRoutes {
    const val driver = "main/driver"
    const val operator = "main/operator"
    const val supervisor = "main/supervisor"
    const val engineer = "main/engineer"
    const val profile = "main/profile/{role}"
    const val fuelRoute = "main/fuel/{role}"
    const val dieselRoute = "main/diesel/{role}"
    const val loadingsRoute = "main/loadings/{role}"
    const val tripsRoute = "main/trips/{role}"

    fun profile(role: UserRole): String = "main/profile/${role.name.lowercase()}"
    fun fuel(role: UserRole): String = "main/fuel/${role.name.lowercase()}"
    fun diesel(role: UserRole): String = "main/diesel/${role.name.lowercase()}"
    fun loadings(role: UserRole): String = "main/loadings/${role.name.lowercase()}"
    fun trips(role: UserRole): String = "main/trips/${role.name.lowercase()}"
}

fun parseUserRoleFromArg(arg: String?): UserRole? = when (arg?.lowercase()) {
    "driver" -> UserRole.Driver
    "operator" -> UserRole.Operator
    "supervisor" -> UserRole.Supervisor
    "engineer" -> UserRole.Engineer
    else -> null
}

fun UserRole.toRoute(): String = when (this) {
    UserRole.Driver -> MainRoutes.driver
    UserRole.Operator -> MainRoutes.operator
    UserRole.Supervisor -> MainRoutes.supervisor
    UserRole.Engineer -> MainRoutes.engineer
}

object NavStateKeys {
    /** Set on the role home back stack entry before popping Profile so Home tab is selected. */
    const val RESET_HOME_BOTTOM_TAB = "svb_reset_home_bottom_tab"
}

/** Pop back to the role home route and select the Home tab (Profile / HSD Fuel / system back). */
fun NavHostController.popRoleHomeWithHomeTabSelected() {
    previousBackStackEntry?.savedStateHandle?.set(NavStateKeys.RESET_HOME_BOTTOM_TAB, true)
    popBackStack()
}

fun UserRole.supportsHsdFuelScreen(): Boolean = when (this) {
    UserRole.Driver, UserRole.Operator, UserRole.Supervisor -> true
    UserRole.Engineer -> false
}

fun UserRole.supportsDieselInventoryScreen(): Boolean = this == UserRole.Engineer

fun UserRole.supportsOperatorLoadingsScreen(): Boolean = this == UserRole.Operator

fun UserRole.supportsDriverTripsScreen(): Boolean = this == UserRole.Driver
