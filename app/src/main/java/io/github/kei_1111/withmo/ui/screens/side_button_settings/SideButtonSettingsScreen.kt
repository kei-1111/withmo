package io.github.kei_1111.withmo.ui.screens.side_button_settings

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoSaveButton
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.side_button_settings.component.SideButtonSettingsScreenContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing")
@Composable
fun SideButtonSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: SideButtonSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    BackHandler {
        viewModel.onEvent(SideButtonSettingsUiEvent.NavigateToSettingsScreen)
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is SideButtonSettingsUiEvent.ChangeIsScaleSliderButtonShown -> {
                    viewModel.changeIsScaleSliderButtonShown(event.isScaleSliderButtonShown)
                }

                is SideButtonSettingsUiEvent.ChangeIsOpenDocumentButtonShown -> {
                    viewModel.changeIsOpenDocumentButtonShown(event.isOpenDocumentButtonShown)
                }

                is SideButtonSettingsUiEvent.ChangeIsSetDefaultModelButtonShown -> {
                    viewModel.changeIsSetDefaultModelButtonShown(event.isSetDefaultModelButtonShown)
                }

                is SideButtonSettingsUiEvent.Save -> {
                    viewModel.saveSideButtonSettings()
                }

                is SideButtonSettingsUiEvent.SaveSuccess -> {
                    showToast(context, "保存しました")
                    latestNavigateToSettingsScreen()
                }

                is SideButtonSettingsUiEvent.SaveFailure -> {
                    showToast(context, "保存に失敗しました")
                }

                is SideButtonSettingsUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    SideButtonSettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun SideButtonSettingsScreen(
    uiState: SideButtonSettingsUiState,
    onEvent: (SideButtonSettingsUiEvent) -> Unit,
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
                content = { TitleLargeText(text = "サイドボタン") },
                navigateBack = { onEvent(SideButtonSettingsUiEvent.NavigateToSettingsScreen) },
            )
            SideButtonSettingsScreenContent(
                uiState = uiState,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(Weights.Medium)
                    .verticalScroll(rememberScrollState()),
            )
            WithmoSaveButton(
                onClick = { onEvent(SideButtonSettingsUiEvent.Save) },
                enabled = uiState.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.Medium),
            )
        }
    }
}
