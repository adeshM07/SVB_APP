package com.svb.fieldops.presentation.viewmodel

import androidx.lifecycle.ViewModel
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
        _uiState.update { it.copy(employeeId = value) }
    }

    fun onUniqueCodeChange(value: String) {
        _uiState.update { it.copy(uniqueCode = value) }
    }

    /** Stub login — later replace with API + session. */
    fun onLoginSubmitted() {
        _uiState.update {
            it.copy(
                siteName = "SVB 68-Acre",
                distanceText = "45m (in zone)",
                statusLabel = "Verified",
                previewLocation = "SVB Site - Zone A",
                previewMachine = "TATA 2518 — #SVB-T019",
            )
        }
    }

    fun onSelfiePlaceholderCaptured() {
        _uiState.update { it.copy(selfieUri = "placeholder://selfie") }
    }

    fun clearSelfie() {
        _uiState.update { it.copy(selfieUri = null) }
    }
}
