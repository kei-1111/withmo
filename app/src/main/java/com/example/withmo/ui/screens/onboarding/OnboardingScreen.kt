package com.example.withmo.ui.screens.onboarding

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.screens.onboarding.content.FinishContent
import com.example.withmo.ui.screens.onboarding.content.SelectDisplayModelContent
import com.example.withmo.ui.screens.onboarding.content.SelectFavoriteAppContent
import com.example.withmo.ui.screens.onboarding.content.WelcomeContent
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.utils.FileUtils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
@Composable
fun OnboardingScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (Environment.isExternalStorageManager()) {
                viewModel.getModelFileList(FileUtils.getModelFile(context))
            }
        },
    )

    if (Environment.isExternalStorageManager()) {
        LaunchedEffect(Unit) {
            viewModel.getModelFileList(FileUtils.getModelFile(context))
        }
    }

    val latestNavigateToHomeScreen by rememberUpdatedState(navigateToHomeScreen)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is OnboardingUiEvent.AddSelectedAppList -> {
                    viewModel.addSelectedAppList(event.appInfo)
                }

                is OnboardingUiEvent.RemoveSelectedAppList -> {
                    viewModel.removeSelectedAppList(event.appInfo)
                }

                is OnboardingUiEvent.OnValueChangeAppSearchQuery -> {
                    viewModel.onValueChangeAppSearchQuery(event.query)
                }

                is OnboardingUiEvent.SelectModelFile -> {
                    viewModel.selectModelFile(event.modelFile)
                }

                is OnboardingUiEvent.RequestExternalStoragePermission -> {
                    launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                }

                is OnboardingUiEvent.NavigateToNextPage -> {
                    viewModel.navigateToNextPage()
                }

                is OnboardingUiEvent.NavigateToPreviousPage -> {
                    viewModel.navigateToPreviousPage()
                }

                is OnboardingUiEvent.OnboardingFinished -> {
                    viewModel.saveFavoriteAppList()
                    uiState.selectedModelFile?.sendPathToUnity()
                    latestNavigateToHomeScreen()
                }
            }
        }.launchIn(this)
    }

    OnboardingScreen(
        appList = appList.toPersistentList(),
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun OnboardingScreen(
    appList: ImmutableList<AppInfo>,
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    Surface(
        modifier = modifier,
    ) {
        when (uiState.currentPage) {
            OnboardingPage.Welcome -> {
                WelcomeContent(
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.SelectFavoriteApp -> {
                SelectFavoriteAppContent(
                    appList = appList,
                    uiState = uiState,
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.SelectDisplayModel -> {
                SelectDisplayModelContent(
                    uiState = uiState,
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.Finish -> {
                FinishContent(
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
        }
    }
}

@Composable
fun OnboardingBottomAppBarNextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .height(UiConfig.SettingItemHeight),
        enabled = enabled,
    ) {
        BodyMediumText(
            text = text,
            color = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha)
            },
        )
    }
}

@Composable
fun OnboardingBottomAppBarPreviousButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(UiConfig.SettingItemHeight),
    ) {
        BodyMediumText(text = "戻る")
    }
}
