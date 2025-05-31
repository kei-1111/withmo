package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import javax.inject.Inject

class GetUserSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetUserSettingsUseCase {
    override operator fun invoke() = userSettingsRepository.userSettings
}
