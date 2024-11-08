package com.example.withmo.domain.usecase.user_settings.sort

import com.example.withmo.domain.model.user_settings.SortSettings

interface SaveSortSettingsUseCase {
    suspend operator fun invoke(sortSettings: SortSettings)
}
