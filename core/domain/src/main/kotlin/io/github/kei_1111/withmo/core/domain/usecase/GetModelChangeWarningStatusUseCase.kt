package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface GetModelChangeWarningStatusUseCase {
    suspend operator fun invoke(): Boolean
}

class GetModelChangeWarningStatusUseCaseImpl @Inject constructor(
    private val oneTimeEventRepository: OneTimeEventRepository,
) : GetModelChangeWarningStatusUseCase {
    override suspend operator fun invoke(): Boolean =
        oneTimeEventRepository.isModelChangeWarningFirstShown.first()
}
