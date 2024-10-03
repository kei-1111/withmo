package com.example.withmo.ui.screens.setting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withmo.domain.model.SettingMode
import com.example.withmo.ui.component.settingscreen.ChooseSettingMode
import com.example.withmo.ui.component.settingscreen.ChoosingHome
import com.example.withmo.ui.component.settingscreen.ChoosingModel
import com.example.withmo.ui.component.settingscreen.home.SettingAppIcon
import com.example.withmo.ui.component.settingscreen.home.SettingClock
import com.example.withmo.ui.component.settingscreen.home.SettingHomeLayout
import com.example.withmo.ui.component.settingscreen.home.SettingNotification
import com.example.withmo.ui.component.settingscreen.model.ModelFileList
import com.example.withmo.ui.theme.Typography
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.toPersistentList

@Suppress("ModifierMissing", "LongMethod")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SettingScreen(
    navigateToHomeScreen: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {},
    ) {
        TextButton(
            onClick = {
                navigateToHomeScreen()
                viewModel.saveUserSetting()
            },
            modifier = Modifier
                .align(Alignment.End),
        ) {
            Text(
                text = "閉じる",
                style = Typography.headlineMedium,
            )
        }
        ChooseSettingMode(
            changeSettingMode = {
                viewModel.setSettingMode(it)
            },
            showFileAccessCheckDialog = uiState.showFileAccessCheckDialog,
            setShowFileAccessCheckDialog = {
                viewModel.setShowFileAccessCheckDialog(it)
            },
            setModelFileList = {
                viewModel.setCurrentUserSetting(
                    currentUserSettings = uiState.currentUserSettings
                        .copy(modelFileList = it),
                )
            },
        )
        when (uiState.settingMode) {
            SettingMode.HOME -> {
                ChoosingHome()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(UiConfig.MediumPadding),
                    verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
                ) {
                    SettingNotification(
                        showNotificationCheckDialog = uiState.showNotificationCheckDialog,
                        setShowNotificationCheckDialog = {
                            viewModel.setShowNotificationCheckDialog(it)
                        },
                        showNotificationAnimation = uiState.currentUserSettings.showNotificationAnimation,
                        setNotificationState = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(showNotificationAnimation = it),
                            )
                        },
                    )
                    SettingClock(
                        showClock = uiState.currentUserSettings.showClock,
                        setShowClock = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(showClock = it),
                            )
                        },
                        clockMode = uiState.currentUserSettings.clockMode,
                        setClockMode = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(clockMode = it),
                            )
                        },
                    )
                    SettingAppIcon(
                        appIconSize = uiState.currentUserSettings.appIconSize,
                        setAppIconSize = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(appIconSize = it),
                            )
                        },
                        appIconPadding = uiState.currentUserSettings.appIconPadding,
                        setAppIconPadding = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(appIconPadding = it),
                            )
                        },
                        showAppName = uiState.currentUserSettings.showAppName,
                        setShowAppName = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(showAppName = it),
                            )
                        },
                    )
                    SettingHomeLayout(
                        showScaleSliderButton = uiState.currentUserSettings.showScaleSliderButton,
                        setScaleSliderButton = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(showScaleSliderButton = it),
                            )
                        },
                        showSortButton = uiState.currentUserSettings.showSortButton,
                        setSortButton = {
                            viewModel.setCurrentUserSetting(
                                currentUserSettings = uiState.currentUserSettings
                                    .copy(showSortButton = it),
                            )
                        },
                    )
                }
            }

            SettingMode.MODEL -> {
                ChoosingModel()
                if (uiState.currentUserSettings.modelFileList.isNotEmpty()) {
                    ModelFileList(
                        modelFileList = uiState.currentUserSettings.modelFileList.toPersistentList(),
                        toHome = {
                            navigateToHomeScreen()
                            viewModel.saveUserSetting()
                        },
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(UiConfig.LargePadding),
                        text = "モデルが見つかりませんでした\nモデルをダウンロードしてください",
                        style = Typography.headlineMedium.copy(
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }
    }
}
