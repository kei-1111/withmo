package com.example.withmo.domain.usecase.user_settings.sort_mode

import com.example.withmo.domain.model.user_settings.SortType

interface SaveSortTypeUseCase {
    suspend operator fun invoke(sortType: SortType)
}
