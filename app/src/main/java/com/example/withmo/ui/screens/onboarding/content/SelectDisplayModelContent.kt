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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.CenteredMessage
import com.example.withmo.ui.component.DisplayModelSettingItem
import com.example.withmo.ui.component.TitleLargeText
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarPreviousButton
import com.example.withmo.ui.screens.onboarding.OnboardingUiEvent
import com.example.withmo.ui.screens.onboarding.OnboardingUiState
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

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
                ModelFileList(
                    modelFileList = uiState.modelFileList,
                    selectedModelFile = uiState.selectedModelFile,
                    selectModelFile = { onEvent(OnboardingUiEvent.SelectModelFile(it)) },
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                PermissionRequiredContent(
                    requestExternalStoragePermission = { onEvent(OnboardingUiEvent.RequestExternalStoragePermission) },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        SelectDisplayModelContentBottomAppBar(
            navigateToPreviousPage = { onEvent(OnboardingUiEvent.NavigateToPreviousPage) },
            navigateToNextPage = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            isSelected = uiState.selectedModelFile != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConfig.MediumPadding),
        )
    }
}

@Composable
fun ModelFileList(
    modelFileList: ImmutableList<ModelFile>,
    selectedModelFile: ModelFile?,
    selectModelFile: (ModelFile) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (modelFileList.isNotEmpty()) {
        LazyColumn(
            modifier = modifier
                .padding(UiConfig.MediumPadding),
            verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
        ) {
            items(modelFileList) {
                DisplayModelSettingItem(
                    fileName = it.fileName,
                    downloadDate = it.downloadDate,
                    onClick = { selectModelFile(it) },
                    selectedModelFile = selectedModelFile,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    } else {
        CenteredMessage(
            message = "VRMファイルが見つかりません",
            modifier = modifier,
        )
    }
}

@Composable
fun PermissionRequiredContent(
    requestExternalStoragePermission: () -> Unit,
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
                .clickable { requestExternalStoragePermission() },
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun SelectDisplayModelContentBottomAppBar(
    navigateToPreviousPage: () -> Unit,
    navigateToNextPage: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = navigateToPreviousPage,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
        OnboardingBottomAppBarNextButton(
            text = if (isSelected) "次へ" else "スキップ",
            onClick = navigateToNextPage,
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
    }
}
