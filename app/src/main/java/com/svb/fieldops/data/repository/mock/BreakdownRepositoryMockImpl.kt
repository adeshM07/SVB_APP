package com.svb.fieldops.data.repository.mock

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.breakdown.BreakdownReportDraft
import com.svb.fieldops.domain.repository.BreakdownRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

@Singleton
class BreakdownRepositoryMockImpl @Inject constructor() : BreakdownRepository {

    override suspend fun submitReport(draft: BreakdownReportDraft): NetworkResult<String> {
        delay(30)
        return NetworkResult.Success("mock-breakdown-id-${draft.typeLabel.hashCode()}")
    }
}
