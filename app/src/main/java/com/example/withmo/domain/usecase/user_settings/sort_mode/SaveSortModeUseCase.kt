package com.example.withmo.domain.usecase.user_settings.sort_mode

import com.example.withmo.domain.model.SortMode

interface SaveSortModeUseCase {
    suspend operator fun invoke(sortMode: SortMode)
}
