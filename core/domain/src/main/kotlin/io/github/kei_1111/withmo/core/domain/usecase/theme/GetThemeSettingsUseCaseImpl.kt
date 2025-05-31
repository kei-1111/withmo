package io.github.kei_1111.withmo.core.domain.usecase.theme

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetThemeSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetThemeSettingsUseCase {
    override operator fun invoke() =
        userSettingsRepository.userSettings
            .map { it.themeSettings }
            .distinctUntilChanged()
}
