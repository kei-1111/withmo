package io.github.kei_1111.withmo.domain.usecase.user_settings

import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import javax.inject.Inject

class GetUserSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetUserSettingsUseCase {
    override suspend operator fun invoke() = userSettingsRepository.userSettings
}
