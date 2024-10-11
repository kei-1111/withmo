package com.example.withmo.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.withmo.R
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.theme.UiConfig

@Composable
fun WithmoTopAppBar(
    currentScreen: Screen,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    val topPaddingValue = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(UiConfig.TopAppBarHeight + topPaddingValue),
        shadowElevation = UiConfig.ShadowElevation,
    ) {
        WithmoTopAppBarContent(
            currentScreen = currentScreen,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPaddingValue),
            navigateBack = navigateBack,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun WithmoTopAppBarContent(
    currentScreen: Screen,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .padding(horizontal = UiConfig.MediumPadding),
        contentAlignment = Alignment.CenterStart,
    ) {
        when (currentScreen) {
            Screen.Settings -> {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { navigateBack() },
                )
            }

            else -> {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable { navigateBack() },
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (currentScreen) {
                Screen.Settings -> {
                    Row(
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
                        TitleLargeText(text = "の設定")
                    }
                }

                Screen.NotificationSettings -> {
                    TitleLargeText(text = "通知")
                }

                Screen.ClockSettings -> {
                    TitleLargeText(text = "時計")
                }

                Screen.AppIconSettings -> {
                    TitleLargeText(text = "アプリアイコン")
                }

                Screen.SideButtonSettings -> {
                    TitleLargeText(text = "サイドボタン")
                }

                Screen.DisplayModelSetting -> {
                    TitleLargeText(text = "表示モデル")
                }

                Screen.ThemeSettings -> {
                    TitleLargeText(text = "テーマ")
                }

                else -> {}
            }
        }
    }
}
