package io.github.kei_1111.withmo.feature.onboarding.component.contents

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.TitleLargeText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoTopAppBar
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.BadgeSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable
import io.github.kei_1111.withmo.feature.onboarding.OnboardingAction
import io.github.kei_1111.withmo.feature.onboarding.OnboardingScreenDimensions
import io.github.kei_1111.withmo.feature.onboarding.OnboardingState
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarPreviousButton
import java.io.File

@RequiresApi(Build.VERSION_CODES.R)
@Composable
internal fun SelectDisplayModelContent(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onAction(OnboardingAction.OnPreviousButtonClick)
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
                state = state,
                onClick = { onAction(OnboardingAction.OnSelectDisplayModelAreaClick) },
            )
            SelectedModelFileName(
                fileName = if (state.modelFilePath.path != null) {
                    File(state.modelFilePath.path).name
                } else {
                    "デフォルトモデル"
                },
            )
        }
        SelectDisplayModelContentBottomAppBar(
            state = state,
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
        )
    }
}

@Composable
private fun SelectDisplayModelArea(
    state: OnboardingState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isModelFileThumbnail = state.modelFileThumbnail != null

    val selectDisplayModelAreaModifier = if (isModelFileThumbnail) {
        modifier
            .size(OnboardingScreenDimensions.SelectDisplayModelAreaSize)
            .border(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
                width = OnboardingScreenDimensions.BorderWidth,
            )
            .safeClickable { onClick() }
    } else {
        modifier
            .size(OnboardingScreenDimensions.SelectDisplayModelAreaSize)
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium,
                strokeWidth = OnboardingScreenDimensions.BorderWidth,
                gapLength = OnboardingScreenDimensions.SelectDisplayModelAreaGapLength,
            )
            .safeClickable { onClick() }
    }

    Surface(
        modifier = selectDisplayModelAreaModifier,
        shape = MaterialTheme.shapes.medium,
    ) {
        if (isModelFileThumbnail) {
            Image(
                bitmap = state.modelFileThumbnail!!.asImageBitmap(),
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
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isModelSelected = state.modelFilePath.path != null

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onAction(OnboardingAction.OnPreviousButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = if (isModelSelected) "次へ" else "スキップ",
            enabled = !state.isModelLoading,
            onClick = { onAction(OnboardingAction.OnNextButtonClick) },
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

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SelectDisplayModelContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectDisplayModelContent(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SelectDisplayModelContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectDisplayModelContent(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SelectDisplayModelContentBottomAppBarLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectDisplayModelContentBottomAppBar(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun SelectDisplayModelContentBottomAppBarDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectDisplayModelContentBottomAppBar(
            state = OnboardingState(),
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
