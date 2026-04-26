package com.svb.fieldops.domain.model.breakdown

/**
 * Domain shape for submitting a breakdown report. Screens keep their own UI state until wired.
 */
data class BreakdownReportDraft(
    val severity: BreakdownSeverity,
    val typeLabel: String,
    val notes: String,
)
