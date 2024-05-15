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
import androidx.compose.ui.graphics.Color
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
import com.example.withmo.until.CONTENT_PADDING
import com.example.withmo.until.MEDIUM_SPACE

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    hideSetting: () -> Unit,
) {
    val uiState = settingViewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {}
    ) {
        TextButton(
            onClick = {
                hideSetting()
                settingViewModel.saveUserSetting()
            },
            modifier = Modifier
                .align(Alignment.End),
        ) {
            Text(
                text = "閉じる",
                style = Typography.headlineMedium
            )
        }
        ChooseSettingMode(
            changeSettingMode = {
                settingViewModel.setSettingMode(it)
            },
            showFileAccessCheckDialog = uiState.showFileAccessCheckDialog,
            setShowFileAccessCheckDialog = {
                settingViewModel.setShowFileAccessCheckDialog(it)
            },
            setModelFileList = {
                settingViewModel.setCurrentUserSetting(
                    currentUserSetting = uiState.currentUserSetting
                        .copy(modelFileList = it)
                )
            }
        )
        when (uiState.settingMode) {
            SettingMode.HOME -> {
                ChoosingHome()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(CONTENT_PADDING),
                    verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING)
                ) {
                    SettingNotification(
                        showNotificationCheckDialog = uiState.showNotificationCheckDialog,
                        setShowNotificationCheckDialog = {
                            settingViewModel.setShowNotificationCheckDialog(it)
                        },
                        showNotificationAnimation = uiState.currentUserSetting.showNotificationAnimation,
                        setNotificationState = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(showNotificationAnimation = it)
                            )
                        }
                    )
                    SettingClock(
                        showClock = uiState.currentUserSetting.showClock,
                        setShowClock = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(showClock = it)
                            )
                        },
                        clockMode = uiState.currentUserSetting.clockMode,
                        setClockMode = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(clockMode = it)
                            )
                        }
                    )
                    SettingAppIcon(
                        appIconSize = uiState.currentUserSetting.appIconSize,
                        setAppIconSize = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(appIconSize = it)
                            )
                        },
                        appIconPadding = uiState.currentUserSetting.appIconPadding,
                        setAppIconPadding = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(appIconPadding = it)
                            )
                        },
                        showAppName = uiState.currentUserSetting.showAppName,
                        setShowAppName = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(showAppName = it)
                            )
                        }
                    )
                    SettingHomeLayout(
                        showScaleSliderButton = uiState.currentUserSetting.showScaleSliderButton,
                        setScaleSliderButton = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(showScaleSliderButton = it)
                            )
                        },
                        showSortButton = uiState.currentUserSetting.showSortButton,
                        setSortButton = {
                            settingViewModel.setCurrentUserSetting(
                                currentUserSetting = uiState.currentUserSetting
                                    .copy(showSortButton = it)
                            )
                        }
                    )
                }
            }

            SettingMode.MODEL -> {
                ChoosingModel()
                if (uiState.currentUserSetting.modelFileList.isNotEmpty()) {
                    ModelFileList(
                        modelFileList = uiState.currentUserSetting.modelFileList,
                        toHome = {
                            hideSetting()
                            settingViewModel.saveUserSetting()
                        }
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CONTENT_PADDING + MEDIUM_SPACE),
                        text = "モデルが見つかりませんでした\nモデルをダウンロードしてください",
                        style = Typography.headlineMedium.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}