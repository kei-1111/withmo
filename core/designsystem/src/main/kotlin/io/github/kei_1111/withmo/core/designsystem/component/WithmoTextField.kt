
package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

private val TextFieldHeight = 36.dp

@Composable
fun WithmoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.onSurface),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle.copy(
            color = textColor,
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
    ) { innerTextField ->
        Box(
            contentAlignment = Alignment.CenterStart,
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle,
                    color = placeholderColor,
                )
            }
            innerTextField()
        }
    }
}

@Composable
fun WithmoSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    Surface(
        modifier = modifier.height(TextFieldHeight),
        color = backgroundColor,
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Paddings.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Search",
                modifier = Modifier.size(IconSizes.Medium),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            WithmoTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(Weights.Medium),
                placeholder = "検索",
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )
        }
    }
}

@Preview
@Composable
private fun WithmoTextFieldLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoTextField(
            value = "",
            onValueChange = {},
            placeholder = "Search",
            modifier = Modifier.height(TextFieldHeight),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { /* Handle search action */ }),
        )
    }
}

@Preview
@Composable
private fun WithmoTextFieldDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoTextField(
            value = "",
            onValueChange = {},
            placeholder = "Search",
            modifier = Modifier.height(TextFieldHeight),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { /* Handle search action */ }),
        )
    }
}

@Preview
@Composable
private fun WithmoSearchTextFieldLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoSearchTextField(
            value = "",
            onValueChange = {},
        )
    }
}

@Preview
@Composable
private fun WithmoSearchTextFieldDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoSearchTextField(
            value = "",
            onValueChange = {},
        )
    }
}
