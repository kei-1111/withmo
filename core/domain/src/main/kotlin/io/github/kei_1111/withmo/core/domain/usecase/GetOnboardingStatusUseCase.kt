package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface GetOnboardingStatusUseCase {
    suspend operator fun invoke(): Boolean
}

class GetOnboardingStatusUseCaseImpl @Inject constructor(
    private val oneTimeEventRepository: OneTimeEventRepository,
) : GetOnboardingStatusUseCase {
    override suspend operator fun invoke(): Boolean =
        oneTimeEventRepository.isOnboardingFirstShown.first()
}
