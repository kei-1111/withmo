package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface GetFavoriteAppsUseCase {
    operator fun invoke(): Flow<Result<List<FavoriteAppInfo>>>
}

class GetFavoriteAppsUseCaseImpl @Inject constructor(
    private val favoriteAppRepository: FavoriteAppRepository,
) : GetFavoriteAppsUseCase {
    override operator fun invoke(): Flow<Result<List<FavoriteAppInfo>>> =
        favoriteAppRepository.favoriteAppsInfo
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
}
