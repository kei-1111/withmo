package com.example.withmo.domain.usecase.user_settings.app_icon

import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAppIconSettingsUseCaseImpl @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository,
) : GetAppIconSettingsUseCase {
    override suspend fun invoke(): Flow<AppIconSettings> =
        userSettingsRepository.userSettings.map { it.appIconSettings }
}
