package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetThemeSettingsUseCase {
    operator fun invoke(): Flow<Result<ThemeSettings>>
}

class GetThemeSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetThemeSettingsUseCase {
    override operator fun invoke(): Flow<Result<ThemeSettings>> =
        userSettingsRepository.userSettings
            .map { result ->
                result.map { it.themeSettings }
            }
            .distinctUntilChanged()
}
