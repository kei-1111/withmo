package io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon

import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAppIconSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetAppIconSettingsUseCase {
    override fun invoke(): Flow<AppIconSettings> =
        userSettingsRepository.userSettings
            .map { it.appIconSettings }
            .distinctUntilChanged()
}
