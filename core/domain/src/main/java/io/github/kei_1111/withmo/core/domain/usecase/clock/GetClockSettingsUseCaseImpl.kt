package io.github.kei_1111.withmo.core.domain.usecase.clock

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetClockSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetClockSettingsUseCase {
    override operator fun invoke() =
        userSettingsRepository.userSettings
            .map { it.clockSettings }
            .distinctUntilChanged()
}
