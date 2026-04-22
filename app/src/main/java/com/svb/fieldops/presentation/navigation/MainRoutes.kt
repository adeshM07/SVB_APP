package com.svb.fieldops.presentation.navigation

import com.svb.fieldops.domain.model.UserRole

object MainRoutes {
    const val driver = "main/driver"
    const val operator = "main/operator"
    const val supervisor = "main/supervisor"
    const val engineer = "main/engineer"
    const val profile = "main/profile/{role}"

    fun profile(role: UserRole): String = "main/profile/${role.name.lowercase()}"
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
