package io.github.kei_1111.withmo.feature.home

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.util.AppUtils
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.feature.home.component.AppListSheet
import io.github.kei_1111.withmo.feature.home.component.HomeScreenContent
import io.github.kei_1111.withmo.feature.home.component.ModelChangeWarningDialog
import io.github.kei_1111.withmo.feature.home.component.ModelLoading
import io.github.kei_1111.withmo.feature.home.component.WidgetListSheet
import io.github.kei_1111.withmo.feature.home.component.WidgetResizeBottomSheet
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment
import kotlinx.coroutines.launch

@Suppress("ModifierMissing", "LongMethod", "CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    onNavigateSettingsButtonClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val widgetListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        viewModel.onAction(HomeAction.OnOpenDocumentLauncherResult(uri))
    }

    val configureWidgetLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        viewModel.onAction(HomeAction.OnConfigureWidgetLauncherResult(result))
    }

    val bindWidgetLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        viewModel.onAction(HomeAction.OnBindWidgetLauncherResult(result))
    }

    val currentOnNavigateSettingsButtonClick by rememberUpdatedState(onNavigateSettingsButtonClick)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.OpenDocument -> openDocumentLauncher.launch(arrayOf("*/*"))

                is HomeEffect.ConfigureWidget -> configureWidgetLauncher.launch(effect.intent)

                is HomeEffect.BindWidget -> bindWidgetLauncher.launch(effect.intent)

                is HomeEffect.LaunchApp -> {
                    if (effect.appInfo.packageName == context.packageName) {
                        currentOnNavigateSettingsButtonClick()
                    } else {
                        effect.appInfo.launch(context)
                    }
                }

                is HomeEffect.DeleteApp -> {
                    val isSystemApp = AppUtils.isSystemApp(context, effect.appInfo.packageName)
                    val isProtectedApp = AppUtils.isProtectedApp(context, effect.appInfo.packageName)

                    if (isSystemApp || isProtectedApp) {
                        showToast(context, "システムアプリは削除できません")
                    } else {
                        effect.appInfo.delete(context = context)
                    }
                }

                is HomeEffect.ShowAppListSheet -> scope.launch { appListSheetState.show() }

                is HomeEffect.HideAppListSheet -> scope.launch { appListSheetState.hide() }

                is HomeEffect.ShowWidgetListSheet -> scope.launch { widgetListSheetState.show() }

                is HomeEffect.HideWidgetListSheet -> scope.launch { widgetListSheetState.hide() }

                is HomeEffect.NavigateSettings -> currentOnNavigateSettingsButtonClick()

                is HomeEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        appListSheetState = appListSheetState,
        widgetListSheetState = widgetListSheetState,
        modifier = Modifier.fillMaxSize(),
    )
}

@Suppress("LongMethod", "MultipleEmitters")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    appListSheetState: SheetState,
    widgetListSheetState: SheetState,
    modifier: Modifier = Modifier,
) {
    val topPaddingValue = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding()
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    if (state.isAppListSheetOpened) {
        AppListSheet(
            appListSheetState = appListSheetState,
            state = state,
            onAction = onAction,
        )
    }

    if (state.isWidgetListSheetOpened) {
        WidgetListSheet(
            widgetListSheetState = widgetListSheetState,
            onAction = onAction,
            modifier = Modifier.padding(
                top = topPaddingValue,
            ),
        )
    }

    if (state.isWidgetResizing) {
        state.resizingWidget?.let { widgetInfo ->
            WidgetResizeBottomSheet(
                withmoWidgetInfo = widgetInfo,
                close = { onAction(HomeAction.OnWidgetResizeBottomSheetClose(it)) },
            )
        }
    }

    Box(
        modifier = modifier
            .padding(
                top = topPaddingValue,
                bottom = bottomPaddingValue + Paddings.Medium,
            ),
    ) {
        HomeScreenContent(
            state = state,
            onAction = onAction,
            modifier = Modifier.fillMaxSize(),
        )
    }

    if (state.isModelChangeWarningDialogShown) {
        ModelChangeWarningDialog(
            onConfirm = { onAction(HomeAction.OnModelChangeWarningDialogConfirm) },
            onDismiss = { onAction(HomeAction.OnModelChangeWarningDialogDismiss) },
        )
    }

    if (state.isModelLoading) {
        ModelLoading(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeScreenLightPreview() {
    HomeLightPreviewEnvironment {
        HomeScreen(
            state = HomeState(),
            onAction = {},
            appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            widgetListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun HomeScreenDarkPreview() {
    HomeDarkPreviewEnvironment {
        HomeScreen(
            state = HomeState(),
            onAction = {},
            appListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            widgetListSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        )
    }
}
