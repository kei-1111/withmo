package com.example.withmo.ui.screens.settings

import android.content.Intent
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.until.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing", "LongMethod")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SettingsScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToNotificationSettingsScreen: () -> Unit,
    navigateToClockSettingsScreen: () -> Unit,
    navigateToAppIconSettingsScreen: () -> Unit,
    navigateToSideButtonSettingsScreen: () -> Unit,
    navigateToDisplayModelSettingScreen: () -> Unit,
    navigateToThemeSettingsScreen: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToDisplayModelSettingScreen by rememberUpdatedState(
        navigateToDisplayModelSettingScreen,
    )

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                if (Environment.isExternalStorageManager()) {
                    latestNavigateToDisplayModelSettingScreen()
                }
            },
        )

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is SettingsUiEvent.NavigateToDisplayModelSettingScreen -> {
                    if (Environment.isExternalStorageManager()) {
                        latestNavigateToDisplayModelSettingScreen()
                    } else {
                        viewModel.changeIsFileAccessPermissionDialogShown(true)
                    }
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

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()),
        ) {
            WithmoTopAppBar(
                currentScreen = Screen.Settings,
                navigateBack = navigateToHomeScreen,
            )
            SettingsScreenContent(
                modifier = Modifier.fillMaxSize(),
                navigateToNotificationSettingsScreen = navigateToNotificationSettingsScreen,
                navigateToClockSettingsScreen = navigateToClockSettingsScreen,
                navigateToAppIconSettingsScreen = navigateToAppIconSettingsScreen,
                navigateToSideButtonSettingsScreen = navigateToSideButtonSettingsScreen,
                navigateToDisplayModelSettingScreen = {
                    viewModel.onEvent(SettingsUiEvent.NavigateToDisplayModelSettingScreen)
                },
                navigateToThemeSettingsScreen = navigateToThemeSettingsScreen,
            )
        }
    }

    if (uiState.isFileAccessPermissionDialogShown) {
        FileAccessPermissionDialog(
            onConfirm = {
                viewModel.onEvent(SettingsUiEvent.FileAccessPermissionDialogOnConfirm)
            },
            onDismiss = {
                viewModel.onEvent(SettingsUiEvent.FileAccessPermissionDialogOnDismiss)
            },
        )
    }
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.surface)
//            .pointerInput(Unit) {},
//    ) {
//        TextButton(
//            onClick = {
//                navigateToHomeScreen()
//                viewModel.saveUserSetting()
//            },
//            modifier = Modifier
//                .align(Alignment.End),
//        ) {
//            Text(
//                text = "閉じる",
//                style = Typography.headlineMedium,
//            )
//        }
//        ChooseSettingMode(
//            changeSettingMode = {
//                viewModel.setSettingMode(it)
//            },
//            showFileAccessCheckDialog = uiState.showFileAccessCheckDialog,
//            setShowFileAccessCheckDialog = {
//                viewModel.setShowFileAccessCheckDialog(it)
//            },
//            setModelFileList = {
//                viewModel.setCurrentUserSetting(
//                    currentUserSettings = uiState.currentUserSettings
//                        .copy(modelFileList = it),
//                )
//            },
//        )
//        when (uiState.settingMode) {
//            SettingMode.HOME -> {
//                ChoosingHome()
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState())
//                        .padding(UiConfig.MediumPadding),
//                    verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
//                ) {
//                    SettingNotification(
//                        showNotificationCheckDialog = uiState.showNotificationCheckDialog,
//                        setShowNotificationCheckDialog = {
//                            viewModel.setShowNotificationCheckDialog(it)
//                        },
//                        showNotificationAnimation = uiState.currentUserSettings.showNotificationAnimation,
//                        setNotificationState = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(showNotificationAnimation = it),
//                            )
//                        },
//                    )
//                    SettingClock(
//                        showClock = uiState.currentUserSettings.showClock,
//                        setShowClock = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(showClock = it),
//                            )
//                        },
//                        clockMode = uiState.currentUserSettings.clockMode,
//                        setClockMode = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(clockMode = it),
//                            )
//                        },
//                    )
//                    SettingAppIcon(
//                        appIconSize = uiState.currentUserSettings.appIconSize,
//                        setAppIconSize = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(appIconSize = it),
//                            )
//                        },
//                        appIconPadding = uiState.currentUserSettings.appIconPadding,
//                        setAppIconPadding = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(appIconPadding = it),
//                            )
//                        },
//                        showAppName = uiState.currentUserSettings.showAppName,
//                        setShowAppName = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(showAppName = it),
//                            )
//                        },
//                    )
//                    SettingHomeLayout(
//                        showScaleSliderButton = uiState.currentUserSettings.showScaleSliderButton,
//                        setScaleSliderButton = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(showScaleSliderButton = it),
//                            )
//                        },
//                        showSortButton = uiState.currentUserSettings.showSortButton,
//                        setSortButton = {
//                            viewModel.setCurrentUserSetting(
//                                currentUserSettings = uiState.currentUserSettings
//                                    .copy(showSortButton = it),
//                            )
//                        },
//                    )
//                }
//            }
//
//            SettingMode.MODEL -> {
//                ChoosingModel()
//                if (uiState.currentUserSettings.modelFileList.isNotEmpty()) {
//                    ModelFileList(
//                        modelFileList = uiState.currentUserSettings.modelFileList.toPersistentList(),
//                        toHome = {
//                            navigateToHomeScreen()
//                            viewModel.saveUserSetting()
//                        },
//                    )
//                } else {
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(UiConfig.LargePadding),
//                        text = "モデルが見つかりませんでした\nモデルをダウンロードしてください",
//                        style = Typography.headlineMedium.copy(
//                            textAlign = TextAlign.Center,
//                        ),
//                    )
//                }
//            }
//        }
//    }
}
