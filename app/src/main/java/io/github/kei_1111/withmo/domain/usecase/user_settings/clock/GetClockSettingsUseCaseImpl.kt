package io.github.kei_1111.withmo.domain.usecase.user_settings.clock

import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetClockSettingsUseCase {
    override operator fun invoke() = userSettingsRepository.userSettings.map { it.clockSettings }
}
