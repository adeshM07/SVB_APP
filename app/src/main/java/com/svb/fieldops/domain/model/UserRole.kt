package com.svb.fieldops.domain.model

/**
 * Field Ops staff roles. Role is fixed for the session after login.
 * Backend will return this on real auth; until then [com.svb.fieldops.domain.auth.DemoStaffResolver] maps demo IDs.
 */
enum class UserRole {
    Driver,
    Operator,
    Supervisor,
    Engineer,
}
