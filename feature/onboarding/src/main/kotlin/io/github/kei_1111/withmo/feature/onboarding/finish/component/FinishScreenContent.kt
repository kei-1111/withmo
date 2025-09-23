package io.github.kei_1111.withmo.feature.onboarding.finish.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.DisplayMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.onboarding.R

@Composable
internal fun FinishScreenContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(Paddings.Medium),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_finish_logo),
            contentDescription = "Finish",
            modifier = Modifier.size(250.dp),
        )
        DisplayMediumText("Completed!")
        BodyMediumText("設定が完了しました！")
    }
}

@Composable
@Preview
private fun FinishScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        FinishScreenContent()
    }
}

@Composable
@Preview
private fun FinishScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        FinishScreenContent()
    }
}
