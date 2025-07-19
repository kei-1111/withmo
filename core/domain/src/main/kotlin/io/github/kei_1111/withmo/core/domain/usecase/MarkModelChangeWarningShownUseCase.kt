package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import javax.inject.Inject

interface MarkModelChangeWarningShownUseCase {
    suspend operator fun invoke()
}

class MarkModelChangeWarningShownUseCaseImpl @Inject constructor(
    private val oneTimeEventRepository: OneTimeEventRepository,
) : MarkModelChangeWarningShownUseCase {
    override suspend operator fun invoke() =
        oneTimeEventRepository.markModelChangeWarningFirstShown()
}
