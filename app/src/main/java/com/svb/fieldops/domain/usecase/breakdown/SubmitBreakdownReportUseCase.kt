package com.svb.fieldops.domain.usecase.breakdown

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.breakdown.BreakdownReportDraft
import com.svb.fieldops.domain.repository.BreakdownRepository
import javax.inject.Inject

class SubmitBreakdownReportUseCase @Inject constructor(
    private val breakdownRepository: BreakdownRepository,
) {
    suspend operator fun invoke(draft: BreakdownReportDraft): NetworkResult<String> =
        breakdownRepository.submitReport(draft)
}
