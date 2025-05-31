package io.github.kei_1111.withmo.core.domain.usecase.sort

import io.github.kei_1111.withmo.core.model.user_settings.SortSettings

interface SaveSortSettingsUseCase {
    suspend operator fun invoke(sortSettings: SortSettings)
}
