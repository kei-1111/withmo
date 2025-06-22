package io.github.kei_1111.withmo.core.domain.manager

interface AppUsageStatsManager {
    fun getAppUsageCounts(): Map<String, Int>
}
