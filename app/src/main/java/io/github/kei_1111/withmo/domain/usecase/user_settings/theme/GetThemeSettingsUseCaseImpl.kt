package io.github.kei_1111.withmo.domain.usecase.user_settings.theme

import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetThemeSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetThemeSettingsUseCase {
    override suspend operator fun invoke() =
        userSettingsRepository.userSettings.map { it.themeSettings }
}
