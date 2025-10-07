package io.github.kei_1111.withmo.feature.setting.screen.sort

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface SortSettingsEffect : Effect {
    data object NavigateBack : SortSettingsEffect
    data class ShowToast(val message: String) : SortSettingsEffect
    data object RequestUsageStatsPermission : SortSettingsEffect
}
