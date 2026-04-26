package com.svb.fieldops.data.config

import com.svb.fieldops.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Central place to read mock vs remote toggles from [BuildConfig].
 * When backend is ready, flip [BuildConfig.USE_MOCK_DATA_SOURCE] per build type or wire a remote flag.
 */
@Singleton
class DataSourceConfiguration @Inject constructor() {
    val useMockDataSource: Boolean get() = BuildConfig.USE_MOCK_DATA_SOURCE
}
