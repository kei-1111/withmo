package io.github.kei_1111.withmo.domain.repository

import kotlinx.coroutines.flow.Flow

interface OneTimeEventRepository {
    val isOnboardingFirstShown: Flow<Boolean>
    val isModelChangeWarningFirstShown: Flow<Boolean>

    suspend fun markOnboardingFirstShown()

    suspend fun markModelChangeWarningFirstShown()
}
