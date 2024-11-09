package com.example.withmo.ui.screens.onboarding.content

import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.component.display_model_setting.DisplayModelSelector
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarPreviousButton
import com.example.withmo.ui.screens.onboarding.OnboardingUiEvent
import com.example.withmo.ui.screens.onboarding.OnboardingUiState
import com.example.withmo.ui.theme.UiConfig

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SelectDisplayModelContent(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isExternalStorageManager by rememberUpdatedState(Environment.isExternalStorageManager())

    Column(
        modifier = modifier,
    ) {
        WithmoTopAppBar(content = { TitleLargeText("表示モデルしたいモデルは？") })
        Box(
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        ) {
            if (isExternalStorageManager) {
                DisplayModelSelector(
                    modelFileList = uiState.modelFileList,
                    selectedModelFile = uiState.selectedModelFile,
                    selectModelFile = { onEvent(OnboardingUiEvent.SelectModelFile(it)) },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                PermissionRequiredContent(
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        SelectDisplayModelContentBottomAppBar(
            uiState = uiState,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConfig.MediumPadding),
        )
    }
}

@Composable
private fun PermissionRequiredContent(
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(UiConfig.MediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BodyMediumText(text = "表示モデル選択のためには、権限が必要です")
        BodyMediumText(text = "設定に行き権限を許可してください")
        BodyMediumText(
            text = "設定へ",
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onEvent(OnboardingUiEvent.RequestExternalStoragePermission) },
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun SelectDisplayModelContentBottomAppBar(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingUiEvent.NavigateToPreviousPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
        OnboardingBottomAppBarNextButton(
            text = if (uiState.selectedModelFile != null) "次へ" else "スキップ",
            onClick = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
    }
}
