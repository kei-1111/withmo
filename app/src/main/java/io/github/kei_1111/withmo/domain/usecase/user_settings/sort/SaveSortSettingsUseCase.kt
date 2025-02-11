package io.github.kei_1111.withmo.domain.usecase.user_settings.sort

import io.github.kei_1111.withmo.domain.model.user_settings.SortSettings

interface SaveSortSettingsUseCase {
    suspend operator fun invoke(sortSettings: SortSettings)
}
