package com.svb.fieldops.data.repository.mock

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.fuel.HsdRequestSummary
import com.svb.fieldops.domain.repository.FuelRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FuelRepositoryMockImpl @Inject constructor() : FuelRepository {

    override suspend fun listPendingHsdRequests(): NetworkResult<List<HsdRequestSummary>> {
        return NetworkResult.Success(
            listOf(
                HsdRequestSummary(id = "mock-hsd-1", title = "Mock HSD request"),
            ),
        )
    }
}
