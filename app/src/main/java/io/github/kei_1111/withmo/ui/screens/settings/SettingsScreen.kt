package io.github.kei_1111.withmo.ui.screens.settings

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.github.kei_1111.withmo.R
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.settings.component.SettingsScreenContent
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.utils.AppUtils
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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

    BackHandler {
        viewModel.onAction(SettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(Unit) {
        viewModel.changeIsDefaultHomeApp(context.packageName == AppUtils.getHomeAppName(context))
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.action.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is SettingsAction.OnNavigateHomeAppSettingButtonClick -> {
                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                    context.startActivity(intent)
                }

                is SettingsAction.OnNavigateClockSettingsButtonClick -> {
                    currentOnNavigateClockSettingsButtonClick()
                }

                is SettingsAction.OnNavigateAppIconSettingsButtonClick -> {
                    currentOnNavigateAppIconSettingsButtonClick()
                }

                is SettingsAction.OnNavigateFavoriteAppSettingsButtonClick -> {
                    currentOnNavigateFavoriteAppSettingsButtonClick()
                }

                is SettingsAction.OnNavigateSideButtonSettingsButtonClick -> {
                    currentOnNavigateSideButtonSettingsButtonClick()
                }

                is SettingsAction.OnNavigateSortSettingsButtonClick -> {
                    currentOnNavigateSortSettingsButtonClick()
                }

                is SettingsAction.OnNavigateNotificationSettingsButtonClick -> {
                    currentOnNavigateNotificationSettingsButtonClick()
                }

                is SettingsAction.OnNavigateThemeSettingsButtonClick -> {
                    currentOnNavigateThemeSettingsButtonClick()
                }

                is SettingsAction.OnBackButtonClick -> {
                    currentOnBackButtonClick()
                }
            }
        }.launchIn(this)
    }

    SettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@Suppress("LongMethod")
@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsAction) -> Unit,
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
                navigateClose = { onEvent(SettingsAction.OnBackButtonClick) },
            )
            SettingsScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                uiState = uiState,
                onEvent = onEvent,
            )
        }
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
