package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import javax.inject.Inject

interface MarkOnboardingShownUseCase {
    suspend operator fun invoke()
}

class MarkOnboardingShownUseCaseImpl @Inject constructor(
    private val oneTimeEventRepository: OneTimeEventRepository,
) : MarkOnboardingShownUseCase {
    override suspend operator fun invoke() =
        oneTimeEventRepository.markOnboardingFirstShown()
}
