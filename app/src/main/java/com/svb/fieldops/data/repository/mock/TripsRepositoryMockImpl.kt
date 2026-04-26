package com.svb.fieldops.data.repository.mock

import com.svb.fieldops.core.common.NetworkResult
import com.svb.fieldops.domain.model.trips.TripSummary
import com.svb.fieldops.domain.repository.TripsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripsRepositoryMockImpl @Inject constructor() : TripsRepository {

    override suspend fun listTodayTrips(): NetworkResult<List<TripSummary>> {
        return NetworkResult.Success(
            listOf(
                TripSummary(id = "mock-trip-1", label = "Mock trip"),
            ),
        )
    }
}
