package com.example.withmo.ui.screens.favorite_app_settings

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
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoSaveButton
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.utils.showToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing")
@Composable
fun FavoriteAppSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: FavoriteAppSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    BackHandler {
        viewModel.onEvent(FavoriteAppSettingsUiEvent.NavigateToSettingsScreen)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is FavoriteAppSettingsUiEvent.AddFavoriteAppList -> {
                    viewModel.addFavoriteAppList(event.appInfo)
                }

                is FavoriteAppSettingsUiEvent.RemoveFavoriteAppList -> {
                    viewModel.removeFavoriteAppList(event.appInfo)
                }

                is FavoriteAppSettingsUiEvent.OnValueChangeAppSearchQuery -> {
                    viewModel.onValueChangeAppSearchQuery(event.query)
                }

                is FavoriteAppSettingsUiEvent.Save -> {
                    viewModel.saveFavoriteAppList()
                }

                is FavoriteAppSettingsUiEvent.SaveSuccess -> {
                    showToast(context, "保存しました")
                    latestNavigateToSettingsScreen()
                }

                is FavoriteAppSettingsUiEvent.SaveFailure -> {
                    showToast(context, "保存に失敗しました")
                }

                is FavoriteAppSettingsUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
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
                navigateBack = { onEvent(FavoriteAppSettingsUiEvent.NavigateToSettingsScreen) },
            )
            FavoriteAppSettingsScreenContent(
                appList = appList,
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(UiConfig.DefaultWeight),
            )
            WithmoSaveButton(
                onClick = { onEvent(FavoriteAppSettingsUiEvent.Save) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(UiConfig.MediumPadding),
            )
        }
    }
}
