package com.svb.fieldops.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.svb.fieldops.domain.auth.DemoStaffResolver
import com.svb.fieldops.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ClockInUiState(
    val employeeId: String = "",
    val uniqueCode: String = "",
    /** Set after successful demo login; drives post–clock-in navigation. */
    val sessionRole: UserRole? = null,
    val loginErrorMessage: String? = null,
    val siteName: String = "SVB 68-Acre",
    val distanceText: String = "45m (in zone)",
    val statusLabel: String = "Verified",
    val previewDate: String = LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM uuuu")),
    val previewTime: String = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")),
    val previewLocation: String = "SVB Site - Zone A",
    val previewMachine: String = "TATA 2518 — #SVB-T019",
    val selfieUri: String? = null,
)

@HiltViewModel
class ClockInFlowViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ClockInUiState())
    val uiState: StateFlow<ClockInUiState> = _uiState.asStateFlow()

    fun onEmployeeIdChange(value: String) {
        _uiState.update { it.copy(employeeId = value, loginErrorMessage = null) }
    }

    fun onUniqueCodeChange(value: String) {
        _uiState.update { it.copy(uniqueCode = value) }
    }

    /**
     * Validates demo Employee ID and sets [ClockInUiState.sessionRole].
     * @return true if login may proceed to geofence; false shows [ClockInUiState.loginErrorMessage].
     */
    fun submitLogin(): Boolean {
        val role = DemoStaffResolver.roleForEmployeeId(_uiState.value.employeeId)
        return if (role != null) {
            _uiState.update {
                it.copy(
                    sessionRole = role,
                    loginErrorMessage = null,
                    siteName = "SVB 68-Acre",
                    distanceText = "45m (in zone)",
                    statusLabel = "Verified",
                    previewLocation = "SVB Site - Zone A",
                    previewMachine = "TATA 2518 — #SVB-T019",
                )
            }
            true
        } else {
            _uiState.update {
                it.copy(
                    loginErrorMessage = "Unknown Employee ID. Demo IDs: OPP001, DVR001, SUP001, ENG001.",
                )
            }
            false
        }
    }

    fun onSelfiePlaceholderCaptured() {
        _uiState.update { it.copy(selfieUri = "placeholder://selfie") }
    }

    fun clearSelfie() {
        _uiState.update { it.copy(selfieUri = null) }
    }
}
