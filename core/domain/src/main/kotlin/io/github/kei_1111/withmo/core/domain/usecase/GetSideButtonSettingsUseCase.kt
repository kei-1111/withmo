package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetSideButtonSettingsUseCase {
    operator fun invoke(): Flow<SideButtonSettings>
}

class GetSideButtonSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetSideButtonSettingsUseCase {
    override operator fun invoke() =
        userSettingsRepository.userSettings
            .map { it.sideButtonSettings }
            .distinctUntilChanged()
}
