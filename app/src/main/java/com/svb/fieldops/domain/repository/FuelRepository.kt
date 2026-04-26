package com.svb.fieldops.domain.repository

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.fuel.HsdRequestSummary

interface FuelRepository {
    suspend fun listPendingHsdRequests(): NetworkResult<List<HsdRequestSummary>>
}
