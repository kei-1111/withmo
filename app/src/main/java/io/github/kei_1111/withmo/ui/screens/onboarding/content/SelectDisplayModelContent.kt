package io.github.kei_1111.withmo.ui.screens.onboarding.content

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiEvent
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiState
import io.github.kei_1111.withmo.ui.theme.UiConfig

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SelectDisplayModelContent(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        WithmoTopAppBar(content = { TitleLargeText("表示モデルしたいモデルは？") })
        Column(
            modifier = Modifier
                .padding(UiConfig.MediumPadding)
                .weight(UiConfig.DefaultWeight),
            verticalArrangement = Arrangement.Center,
        ) {
            TitleLargeText(
                text = "表示モデル選択の注意",
                modifier = Modifier.padding(vertical = UiConfig.MediumPadding),
            )
            BodyMediumText(text = "モデルは、VRMファイルのみ対応しております。")
            BodyMediumText(text = "モデルのVRMファイルを選択してください。")
            BodyMediumText(
                text = "モデルを選択する",
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onEvent(OnboardingUiEvent.OnOpenDocumentButtonClick) },
                color = MaterialTheme.colorScheme.primary,
            )
            BodyMediumText(
                text = "選択中のファイル：${uiState.modelFilePath.path?.substringAfterLast("/") ?: "デフォルト"}",
                modifier = Modifier.padding(vertical = UiConfig.SmallPadding),
            )
        }
        SelectDisplayModelContentBottomAppBar(
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConfig.MediumPadding),
        )
    }
}

@Composable
private fun SelectDisplayModelContentBottomAppBar(
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
            text = "次へ",
            onClick = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
    }
}
