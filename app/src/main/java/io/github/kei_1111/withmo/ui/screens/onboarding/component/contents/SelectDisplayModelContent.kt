package io.github.kei_1111.withmo.ui.screens.onboarding.component.contents

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
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiEvent
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiState
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@RequiresApi(Build.VERSION_CODES.R)
@Composable
internal fun SelectDisplayModelContent(
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
                .padding(Paddings.Medium)
                .weight(Weights.Medium),
            verticalArrangement = Arrangement.Center,
        ) {
            TitleLargeText(
                text = "表示モデル選択の注意",
                modifier = Modifier.padding(vertical = Paddings.Medium),
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
                modifier = Modifier.padding(vertical = Paddings.Small),
            )
        }
        SelectDisplayModelContentBottomAppBar(
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
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
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingUiEvent.NavigateToPreviousPage) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = "次へ",
            onClick = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}
