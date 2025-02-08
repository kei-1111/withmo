package com.example.withmo.ui.screens.settings

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
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
import com.example.withmo.R
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.theme.UiConfig
import com.example.withmo.utils.AppUtils
import com.example.withmo.utils.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing")
@Composable
fun SettingsScreen(
    onNavigate: (Screen) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestOnNavigate by rememberUpdatedState(onNavigate)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (Environment.isExternalStorageManager()) {
                latestOnNavigate(Screen.DisplayModelSetting)
            }
        },
    )

    BackHandler {
        viewModel.onEvent(SettingsUiEvent.OnNavigate(Screen.Home))
    }

    LaunchedEffect(Unit) {
        viewModel.changeIsDefaultHomeApp(context.packageName == AppUtils.getHomeAppName(context))
    }

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is SettingsUiEvent.OnNavigate -> {
                    if (event.screen == Screen.DisplayModelSetting) {
                        if (Environment.isExternalStorageManager()) {
                            latestOnNavigate(Screen.DisplayModelSetting)
                        } else {
                            viewModel.changeIsFileAccessPermissionDialogShown(true)
                        }
                    } else {
                        latestOnNavigate(event.screen)
                    }
                }

                is SettingsUiEvent.SetDefaultHomeApp -> {
                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                    context.startActivity(intent)
                }

                is SettingsUiEvent.FileAccessPermissionDialogOnConfirm -> {
                    viewModel.changeIsFileAccessPermissionDialogShown(false)
                    launcher.launch(Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION))
                }

                is SettingsUiEvent.FileAccessPermissionDialogOnDismiss -> {
                    viewModel.changeIsFileAccessPermissionDialogShown(false)
                    showToast(context, "ファイルアクセス許可が必要です")
                }
            }
        }.launchIn(this)
    }

    SettingsScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@Suppress("LongMethod")
@Composable
private fun SettingsScreen(
    uiState: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
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
                navigateClose = { onEvent(SettingsUiEvent.OnNavigate(Screen.Home)) },
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

    if (uiState.isFileAccessPermissionDialogShown) {
        FileAccessPermissionDialog(
            onConfirm = {
                onEvent(SettingsUiEvent.FileAccessPermissionDialogOnConfirm)
            },
            onDismiss = {
                onEvent(SettingsUiEvent.FileAccessPermissionDialogOnDismiss)
            },
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
                    start = UiConfig.ExtraSmallPadding,
                    end = UiConfig.ExtraSmallPadding,
                    top = UiConfig.SmallPadding,
                    bottom = UiConfig.ExtraSmallPadding,
                ),
        )
        TitleLargeText(text = text)
    }
}
