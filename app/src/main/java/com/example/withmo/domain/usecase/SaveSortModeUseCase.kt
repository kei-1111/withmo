package com.example.withmo.domain.usecase

import com.example.withmo.domain.model.SortMode

interface SaveSortModeUseCase {
    suspend operator fun invoke(sortMode: SortMode)
}
