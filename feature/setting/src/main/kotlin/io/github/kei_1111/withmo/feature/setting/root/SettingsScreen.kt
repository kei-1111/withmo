package io.github.kei_1111.withmo.feature.setting.root

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.util.AppUtils
import io.github.kei_1111.withmo.core.util.showToast
import io.github.kei_1111.withmo.feature.setting.R
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.root.component.NotificationPermissionDialog
import io.github.kei_1111.withmo.feature.setting.root.component.SettingsScreenContent

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing", "CyclomaticComplexMethod")
@Composable
fun SettingsScreen(
    onNavigateClockSettingsButtonClick: () -> Unit,
    onNavigateAppIconSettingsButtonClick: () -> Unit,
    onNavigateFavoriteAppSettingsButtonClick: () -> Unit,
    onNavigateSideButtonSettingsButtonClick: () -> Unit,
    onNavigateSortSettingsButtonClick: () -> Unit,
    onNavigateNotificationSettingsButtonClick: () -> Unit,
    onNavigateThemeSettingsButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val currentOnNavigateClockSettingsButtonClick by rememberUpdatedState(onNavigateClockSettingsButtonClick)
    val currentOnNavigateAppIconSettingsButtonClick by rememberUpdatedState(onNavigateAppIconSettingsButtonClick)
    val currentOnNavigateFavoriteAppSettingsButtonClick by rememberUpdatedState(onNavigateFavoriteAppSettingsButtonClick)
    val currentOnNavigateSideButtonSettingsButtonClick by rememberUpdatedState(onNavigateSideButtonSettingsButtonClick)
    val currentOnNavigateSortSettingsButtonClick by rememberUpdatedState(onNavigateSortSettingsButtonClick)
    val currentOnNavigateNotificationSettingsButtonClick by rememberUpdatedState(onNavigateNotificationSettingsButtonClick)
    val currentOnNavigateThemeSettingsButtonClick by rememberUpdatedState(onNavigateThemeSettingsButtonClick)
    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        viewModel.onAction(SettingsAction.OnNotificationListenerPermissionResult)
    }

    BackHandler {
        viewModel.onAction(SettingsAction.OnBackButtonClick)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START ->
                    viewModel.onAction(SettingsAction.OnSettingsScreenLifecycleChanged(context.packageName == AppUtils.getHomeAppName(context)))

                Lifecycle.Event.ON_STOP ->
                    viewModel.onAction(SettingsAction.OnSettingsScreenLifecycleChanged(context.packageName == AppUtils.getHomeAppName(context)))

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SettingsEffect.OpenHomeAppSettings -> context.startActivity(effect.intent)

                is SettingsEffect.NavigateClockSettings -> currentOnNavigateClockSettingsButtonClick()

                is SettingsEffect.NavigateAppIconSettings -> currentOnNavigateAppIconSettingsButtonClick()

                is SettingsEffect.NavigateFavoriteAppSettings -> currentOnNavigateFavoriteAppSettingsButtonClick()

                is SettingsEffect.NavigateSideButtonSettings -> currentOnNavigateSideButtonSettingsButtonClick()

                is SettingsEffect.NavigateSortSettings -> currentOnNavigateSortSettingsButtonClick()

                is SettingsEffect.NavigateNotificationSettings -> currentOnNavigateNotificationSettingsButtonClick()

                is SettingsEffect.OpenWallpaperSettings -> context.startActivity(effect.intent)

                is SettingsEffect.NavigateThemeSettings -> currentOnNavigateThemeSettingsButtonClick()

                is SettingsEffect.NavigateBack -> currentOnBackButtonClick()

                is SettingsEffect.ShowToast -> showToast(context, effect.message)

                is SettingsEffect.RequestNotificationListenerPermission -> notificationPermissionLauncher.launch(effect.intent)
            }
        }
    }

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Suppress("LongMethod")
@Composable
private fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
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
                content = { LogoWithText("の設定") },
                navigateClose = { onAction(SettingsAction.OnBackButtonClick) },
            )
            SettingsScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                state = state,
                onAction = onAction,
            )
        }
    }

    if (state.isNotificationPermissionDialogVisible) {
        NotificationPermissionDialog(
            onConfirm = { onAction(SettingsAction.OnNotificationPermissionDialogConfirm) },
            onDismiss = { onAction(SettingsAction.OnNotificationPermissionDialogDismiss) },
        )
    }
}

@Composable
private fun LogoWithText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.withmo_logo),
            contentDescription = "withmo Logo",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(
                    start = Paddings.ExtraSmall,
                    end = Paddings.ExtraSmall,
                    top = Paddings.Small,
                    bottom = Paddings.ExtraSmall,
                ),
        )
        TitleLargeText(text = text)
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SettingsScreenLightPreview() {
    SettingLightPreviewEnvironment {
        SettingsScreen(
            state = SettingsState(
                isDefaultHomeApp = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SettingsScreenDarkPreview() {
    SettingDarkPreviewEnvironment {
        SettingsScreen(
            state = SettingsState(
                isDefaultHomeApp = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun LogoWithTextLightPreview() {
    SettingLightPreviewEnvironment {
        LogoWithText(
            text = "の設定",
            modifier = Modifier.padding(Paddings.Medium),
        )
    }
}

@Composable
@Preview
private fun LogoWithTextDarkPreview() {
    SettingDarkPreviewEnvironment {
        LogoWithText(
            text = "の設定",
            modifier = Modifier.padding(Paddings.Medium),
        )
    }
}
