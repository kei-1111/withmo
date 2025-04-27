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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoSaveButton
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.favorite_app_settings.component.FavoriteAppSettingsScreenContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing")
@Composable
fun FavoriteAppSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: FavoriteAppSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    BackHandler {
        viewModel.onEvent(FavoriteAppSettingsUiEvent.OnBackButtonClick)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is FavoriteAppSettingsUiEvent.OnAllAppListAppClick -> {
                    viewModel.addFavoriteAppList(event.appInfo)
                }

                is FavoriteAppSettingsUiEvent.OnFavoriteAppListAppClick -> {
                    viewModel.removeFavoriteAppList(event.appInfo)
                }

                is FavoriteAppSettingsUiEvent.OnAppSearchQueryChange -> {
                    viewModel.onValueChangeAppSearchQuery(event.query)
                }

                is FavoriteAppSettingsUiEvent.OnSaveButtonClick -> {
                    viewModel.saveFavoriteAppList(
                        onSaveSuccess = {
                            showToast(context, "保存しました")
                            currentOnBackButtonClick()
                        },
                        onSaveFailure = {
                            showToast(context, "保存に失敗しました")
                        },
                    )
                }

                is FavoriteAppSettingsUiEvent.OnBackButtonClick -> {
                    currentOnBackButtonClick()
                }
            }
        }.launchIn(this)
    }

    FavoriteAppSettingsScreen(
        appList = appList.toPersistentList(),
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun FavoriteAppSettingsScreen(
    appList: ImmutableList<AppInfo>,
    uiState: FavoriteAppSettingsUiState,
    onEvent: (FavoriteAppSettingsUiEvent) -> Unit,
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
                navigateBack = { onEvent(FavoriteAppSettingsUiEvent.OnBackButtonClick) },
            )
            FavoriteAppSettingsScreenContent(
                appList = appList,
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Weights.Medium),
            )
            WithmoSaveButton(
                onClick = { onEvent(FavoriteAppSettingsUiEvent.OnSaveButtonClick) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.Medium),
            )
        }
    }
}
