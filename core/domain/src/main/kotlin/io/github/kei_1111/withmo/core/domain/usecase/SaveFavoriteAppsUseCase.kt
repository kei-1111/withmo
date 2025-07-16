package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import javax.inject.Inject

interface SaveFavoriteAppsUseCase {
    suspend operator fun invoke(favoriteApps: List<FavoriteAppInfo>)
}

class SaveFavoriteAppsUseCaseImpl @Inject constructor(
    private val favoriteAppRepository: FavoriteAppRepository,
) : SaveFavoriteAppsUseCase {
    override suspend operator fun invoke(favoriteApps: List<FavoriteAppInfo>) =
        favoriteAppRepository.updateFavoriteApps(favoriteApps)
}
