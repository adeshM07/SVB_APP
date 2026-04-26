package com.svb.fieldops.domain.repository

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.breakdown.BreakdownReportDraft

interface BreakdownRepository {
    suspend fun submitReport(draft: BreakdownReportDraft): NetworkResult<String>
}
