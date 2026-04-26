package com.svb.fieldops.domain.repository

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.trips.TripSummary

interface TripsRepository {
    suspend fun listTodayTrips(): NetworkResult<List<TripSummary>>
}
