package io.github.kei_1111.withmo.core.domain.usecase.sort

import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import kotlinx.coroutines.flow.Flow

interface GetSortSettingsUseCase {
    operator fun invoke(): Flow<SortSettings>
}
