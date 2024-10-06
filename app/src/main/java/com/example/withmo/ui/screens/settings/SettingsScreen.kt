package com.example.withmo.ui.screens.settings

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.WithmoTopAppBar

@Suppress("ModifierMissing", "LongMethod")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SettingsScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToNotificationSettingsScreen: () -> Unit,
    navigateToClockSettingsScreen: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            WithmoTopAppBar(
                currentScreen = Screen.Settings,
                navigateBack = navigateToHomeScreen,
            )
            SettingsScreenContent(
                modifier = Modifier.fillMaxSize(),
                navigateToNotificationSettingsScreen = navigateToNotificationSettingsScreen,
                navigateToClockSettingsScreen = navigateToClockSettingsScreen,
                navigateToAppIconSettingsScreen = {},
                navigateToHomeScreenContentSettingsScreen = {},
                navigateToModelSettingsScreen = {},
            )
        }
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
