package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoSaveButton
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.favorite_app_settings.component.FavoriteAppSettingsScreenContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("ModifierMissing")
@Composable
fun FavoriteAppSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: FavoriteAppSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val appList by viewModel.appList.collectAsStateWithLifecycle()
    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    BackHandler {
        viewModel.onAction(FavoriteAppSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is FavoriteAppSettingsEffect.ShowToast -> showToast(context, effect.message)

                is FavoriteAppSettingsEffect.NavigateBack -> currentOnBackButtonClick()
            }
        }
    }

    FavoriteAppSettingsScreen(
        appList = appList.toPersistentList(),
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun FavoriteAppSettingsScreen(
    appList: ImmutableList<AppInfo>,
    state: FavoriteAppSettingsState,
    onAction: (FavoriteAppSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPaddingValue),
        ) {
            WithmoTopAppBar(
                content = { TitleLargeText(text = "お気に入りアプリ") },
                navigateBack = { onAction(FavoriteAppSettingsAction.OnBackButtonClick) },
            )
            FavoriteAppSettingsScreenContent(
                appList = appList,
                state = state,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Weights.Medium),
            )
            WithmoSaveButton(
                onClick = { onAction(FavoriteAppSettingsAction.OnSaveButtonClick) },
                enabled = state.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.Medium),
            )
        }
    }
}
