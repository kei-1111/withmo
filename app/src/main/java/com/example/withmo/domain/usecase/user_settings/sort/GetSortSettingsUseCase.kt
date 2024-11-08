package com.example.withmo.domain.usecase.user_settings.sort

import com.example.withmo.domain.model.user_settings.SortSettings
import kotlinx.coroutines.flow.Flow

interface GetSortSettingsUseCase {
    suspend operator fun invoke(): Flow<SortSettings>
}
