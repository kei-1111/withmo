package io.github.kei_1111.withmo.feature.home.component.page_content

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoVerticalSlider
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment

@Composable
internal fun ChangeModelScaleContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .safeDrawingPadding()
            .padding(horizontal = Paddings.Medium),
    ) {
        WithmoIconButton(
            onClick = { onAction(HomeAction.OnCloseScaleSliderButtonClick) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(AppConstants.DefaultAppIconSize.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        ScaleSlider(
            state = state,
            onAction = onAction,
            modifier = Modifier.align(Alignment.CenterEnd),
        )
    }
}

@Composable
private fun ScaleSlider(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .withmoShadow(
                shape = MaterialTheme.shapes.medium,
            ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = Paddings.Small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                modifier = Modifier
                    .size(IconSizes.Large),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            WithmoVerticalSlider(
                value = state.currentUserSettings.modelSettings.scale,
                onValueChange = { onAction(HomeAction.OnScaleSliderChange(it)) },
                valueRange = 0.5f..1.5f,
                modifier = Modifier
                    .size(50.dp, 300.dp),
            )
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                modifier = Modifier
                    .size(IconSizes.Small),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ChangeModelScaleContentLightPreview() {
    HomeLightPreviewEnvironment {
        ChangeModelScaleContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ChangeModelScaleContentDarkPreview() {
    HomeDarkPreviewEnvironment {
        ChangeModelScaleContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ScaleSliderLightPreview() {
    HomeLightPreviewEnvironment {
        ScaleSlider(
            state = HomeState(),
            onAction = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ScaleSliderDarkPreview() {
    HomeDarkPreviewEnvironment {
        ScaleSlider(
            state = HomeState(),
            onAction = {},
        )
    }
}
