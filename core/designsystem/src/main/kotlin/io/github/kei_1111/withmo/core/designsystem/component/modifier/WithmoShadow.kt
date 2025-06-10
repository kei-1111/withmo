package io.github.kei_1111.withmo.core.designsystem.component.modifier

import android.graphics.Paint
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 要素の四方にふんわり影を落とす。
 *
 * @param shadowColor 影の色
 * @param alpha       不透明度 (0f‒1f) – [shadowColor] に掛け合わせ
 * @param radius      ぼかし半径
 * @param shape       影を付ける形状
 * @param clip        true で内容も [shape] でクリップ
 */
@Stable
fun Modifier.withmoShadow(
    shadowColor: Color = Color.Black,
    alpha: Float = 0.4f,
    radius: Dp = 2.dp,
    shape: Shape = CircleShape,
    clip: Boolean = true,
): Modifier = this.then(
    Modifier.drawWithCache {
        /* ---------- Outline → Path へ ---------- */
        val outline: Outline = shape.createOutline(size, layoutDirection, this)
        val shadowPath: Path = when (outline) {
            is Outline.Rectangle -> Path().apply { addRect(outline.rect) }
            is Outline.Rounded -> Path().apply { addRoundRect(outline.roundRect) }
            is Outline.Generic -> outline.path
        }

        /* ---------- Android Canvas 用 Paint ---------- */
        val shadowPaint = Paint().apply {
            // 塗り自体は透明（Canvas に実体は描かない）
            setColor(android.graphics.Color.TRANSPARENT)
            setShadowLayer(
                radius.toPx(), // ぼかし半径
                0f,
                0f, // dx, dy = 0 → 四方均等
                shadowColor.copy(alpha = alpha).toArgb(),
            )
        }

        /* ---------- 実際の描画 ---------- */
        onDrawWithContent {
            // 背景レイヤに影を先に描く
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawPath(shadowPath.asAndroidPath(), shadowPaint)
            }

            // clip 指定があれば図形でクリップしてから本体描画
            if (clip) {
                clipPath(shadowPath) {
                    this@onDrawWithContent.drawContent()
                }
            } else {
                drawContent()
            }
        }
    },
)
