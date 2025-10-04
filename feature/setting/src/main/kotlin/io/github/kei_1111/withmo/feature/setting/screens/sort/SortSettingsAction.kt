package io.github.kei_1111.withmo.feature.setting.screens.sort

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.user_settings.SortType

internal sealed interface SortSettingsAction : Action {
    data class OnSortTypeRadioButtonClick(val sortType: SortType) : SortSettingsAction
    data object OnSaveButtonClick : SortSettingsAction
    data object OnBackButtonClick : SortSettingsAction
    data object OnUsageStatsPermissionDialogConfirm : SortSettingsAction
    data object OnUsageStatsPermissionDialogDismiss : SortSettingsAction
    data object OnUsageStatsPermissionResult : SortSettingsAction
}
