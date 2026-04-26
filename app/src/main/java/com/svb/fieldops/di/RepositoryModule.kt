package com.svb.fieldops.di

import com.svb.fieldops.data.repository.mock.BreakdownRepositoryMockImpl
import com.svb.fieldops.data.repository.mock.FuelRepositoryMockImpl
import com.svb.fieldops.data.repository.mock.TripsRepositoryMockImpl
import com.svb.fieldops.domain.repository.BreakdownRepository
import com.svb.fieldops.domain.repository.FuelRepository
import com.svb.fieldops.domain.repository.TripsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBreakdownRepository(impl: BreakdownRepositoryMockImpl): BreakdownRepository

    @Binds
    @Singleton
    abstract fun bindFuelRepository(impl: FuelRepositoryMockImpl): FuelRepository

    @Binds
    @Singleton
    abstract fun bindTripsRepository(impl: TripsRepositoryMockImpl): TripsRepository
}
