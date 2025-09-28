package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetFavoriteAppsUseCase {
    operator fun invoke(): Flow<Result<List<FavoriteAppInfo>>>
}

class GetFavoriteAppsUseCaseImpl @Inject constructor(
    private val favoriteAppRepository: FavoriteAppRepository,
) : GetFavoriteAppsUseCase {
    override operator fun invoke(): Flow<Result<List<FavoriteAppInfo>>> =
        favoriteAppRepository.favoriteAppsInfo
}
