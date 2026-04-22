package com.svb.fieldops.domain.auth

import com.svb.fieldops.domain.model.UserRole

/**
 * Temporary demo login map for UI testing. Replace with API session when backend is ready.
 */
object DemoStaffResolver {

    private val employeeIdToRole: Map<String, UserRole> = mapOf(
        "OPP001" to UserRole.Operator,
        "DVR001" to UserRole.Driver,
        "SUP001" to UserRole.Supervisor,
        "ENG001" to UserRole.Engineer,
    )

    fun roleForEmployeeId(employeeId: String): UserRole? =
        employeeIdToRole[employeeId.trim().uppercase()]
}
