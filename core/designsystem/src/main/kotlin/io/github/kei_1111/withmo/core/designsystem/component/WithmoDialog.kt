package io.github.kei_1111.withmo.core.designsystem.component

import android.R.attr.text
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

/**
 * withmoアプリケーション用のカスタムダイアログコンポーネント
 *
 * Material Design 3に準拠したダイアログで、ユーザーに重要な情報を表示し、
 * アクションの確認を求める際に使用されます。
 *
 * 特徴：
 * - シンプルで分かりやすいレイアウト
 * - アクセシビリティに配慮したデザイン
 * - ライト・ダークテーマ対応
 * - 適切なタッチターゲットサイズ
 *
 * @param title ダイアログのタイトルテキスト。ユーザーに表示される見出し
 * @param description ダイアログの説明文。ユーザーに表示される詳細情報や注意事項
 * @param dismissButtonText キャンセル/却下ボタンに表示されるテキスト（例："キャンセル"）
 * @param confirmButtonText 確認/実行ボタンに表示されるテキスト（例："確認"、"削除"など）
 * @param onDismissRequest ダイアログの外側をタップした時やバックボタンを押した時に呼び出されるコールバック
 * @param onDismissClick キャンセルボタンがクリックされた時に呼び出されるコールバック
 * @param onConfirmClick 確認ボタンがクリックされた時に呼び出されるコールバック
 * @param modifier このダイアログに適用するModifier（オプション）
 *
 * @see WithmoDialogContent
 * @sample WithmoDialogLightPreview
 * @sample WithmoDialogDarkPreview
 */
@Composable
fun WithmoDialog(
    title: String,
    description: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            WithmoDialogContent(
                title = title,
                description = description,
                dismissButtonText = dismissButtonText,
                confirmButtonText = confirmButtonText,
                onConfirmClick = onConfirmClick,
                onDismissClick = onDismissClick,
            )
        }
    }
}

/**
 * WithmoDialogの内部コンテンツコンポーネント
 *
 * ダイアログの実際のコンテンツ（タイトル、説明、ボタン）をレイアウトします。
 * このコンポーネントはプライベートで、WithmoDialogコンポーネント内でのみ使用されます。
 *
 * レイアウト構成：
 * - タイトル（TitleLargeText）
 * - 説明文（BodyMediumText）
 * - ボタン領域（Row配置）
 *
 * @param title タイトルテキスト
 * @param description 説明テキスト
 * @param dismissButtonText キャンセルボタンのテキスト
 * @param confirmButtonText 確認ボタンのテキスト
 * @param onDismissClick キャンセルボタンクリック時のコールバック
 * @param onConfirmClick 確認ボタンクリック時のコールバック
 * @param modifier レイアウト調整用のModifier
 */
@Composable
private fun WithmoDialogContent(
    title: String,
    description: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(20.dp),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        BodyMediumText(description)
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            BodyMediumText(
                text = dismissButtonText,
                modifier = Modifier
                    .safeClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onDismissClick() },
            )
            BodyMediumText(
                text = confirmButtonText,
                modifier = Modifier
                    .safeClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onConfirmClick() },
            )
        }
    }
}

@Composable
@Preview
private fun WithmoDialogLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoDialog(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
            onDismissRequest = { },
        )
    }
}

@Composable
@Preview
private fun WithmoDialogDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoDialog(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
            onDismissRequest = { },
        )
    }
}

@Composable
@Preview
private fun WithmoDialogContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoDialogContent(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
        )
    }
}

@Composable
@Preview
private fun WithmoDialogContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoDialogContent(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
        )
    }
}
