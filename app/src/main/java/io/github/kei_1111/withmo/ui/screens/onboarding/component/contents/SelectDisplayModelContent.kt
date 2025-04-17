package io.github.kei_1111.withmo.ui.screens.onboarding.component.contents

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.component.TitleLargeText
import io.github.kei_1111.withmo.ui.component.WithmoTopAppBar
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingScreenDimensions
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiEvent
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiState
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.theme.dimensions.BadgeSizes
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import java.io.File

@RequiresApi(Build.VERSION_CODES.R)
@Composable
internal fun SelectDisplayModelContent(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onEvent(OnboardingUiEvent.OnPreviousButtonClick)
    }

    Column(
        modifier = modifier,
    ) {
        WithmoTopAppBar(content = { TitleLargeText("表示したいVRMモデルは？") })
        Column(
            modifier = Modifier
                .padding(Paddings.Medium)
                .fillMaxWidth()
                .weight(Weights.Medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SelectDisplayModelArea(
                uiState = uiState,
                onClick = { onEvent(OnboardingUiEvent.OnSelectDisplayModelAreaClick) },
            )
            SelectedModelFileName(
                fileName = if (uiState.modelFilePath.path != null) {
                    File(uiState.modelFilePath.path).name
                } else {
                    "デフォルトモデル"
                },
            )
        }
        SelectDisplayModelContentBottomAppBar(
            uiState = uiState,
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
        )
    }
}

@Composable
private fun SelectDisplayModelArea(
    uiState: OnboardingUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isModelFileThumbnail = uiState.modelFileThumbnail != null

    val selectDisplayModelAreaModifier = if (isModelFileThumbnail) {
        modifier
            .size(OnboardingScreenDimensions.SelectDisplayModelAreaSize)
            .clickable { onClick() }
    } else {
        modifier
            .size(OnboardingScreenDimensions.SelectDisplayModelAreaSize)
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
                strokeWidth = OnboardingScreenDimensions.BorderWidth,
                gapLength = OnboardingScreenDimensions.SelectDisplayModelAreaGapLength,
            )
            .clickable { onClick() }
    }

    Surface(
        modifier = selectDisplayModelAreaModifier,
        shape = MaterialTheme.shapes.medium,
    ) {
        if (isModelFileThumbnail) {
            Image(
                bitmap = uiState.modelFileThumbnail!!.asImageBitmap(),
                contentDescription = "Model Thumbnail",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    Paddings.Medium,
                    Alignment.CenterVertically,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SelectDisplayModelAddBadge()
                SelectDisplayModelDescription()
            }
        }
    }
}

@Composable
private fun SelectDisplayModelAddBadge(
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = {},
        modifier = modifier
            .border(
                width = OnboardingScreenDimensions.BorderWidth,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            )
            .size(BadgeSizes.Large),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add",
        )
    }
}

@Composable
private fun SelectDisplayModelDescription(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BodyMediumText(
            text = "表示モデルの選択",
            color = MaterialTheme.colorScheme.primary,
        )
        LabelMediumText(
            text = "VRMファイルを選択してください",
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SelectedModelFileName(
    fileName: String,
    modifier: Modifier = Modifier,
) {
    BodyMediumText(
        text = "選択中のモデル：$fileName",
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(Paddings.Medium),
    )
}

@Composable
private fun SelectDisplayModelContentBottomAppBar(
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isModelSelected = uiState.modelFilePath.path != null

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingUiEvent.OnPreviousButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = if (isModelSelected) "次へ" else "スキップ",
            enabled = !uiState.isModelLoading,
            onClick = { onEvent(OnboardingUiEvent.OnNextButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 4.dp,
    gapLength: Dp = 8.dp,
    cap: StrokeCap = StrokeCap.Round,
): Modifier = this.drawWithContent {
    val outline = shape.createOutline(size, layoutDirection, density = this)

    val dashedStroke = Stroke(
        cap = cap,
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
        ),
    )

    drawContent()

    drawOutline(
        outline = outline,
        style = dashedStroke,
        brush = SolidColor(color),
    )
}
