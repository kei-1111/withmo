@file:Suppress("ModifierComposed")

package io.github.kei_1111.withmo.core.ui.modifier

import android.R.attr.enabled
import android.R.attr.onClick
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.safeClickable(
    indication: Indication? = ripple(),
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit,
): Modifier = composed {
    val blocker = LocalClickBlocker.current
    val interactionSource = remember { MutableInteractionSource() }
    this.combinedClickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onLongClickLabel = onLongClickLabel,
        onLongClick = {
            if (blocker.tryClick()) { onLongClick?.invoke() }
        },
        onDoubleClick = {
            if (blocker.tryClick()) { onDoubleClick?.invoke() }
        },
        onClick = {
            if (blocker.tryClick()) { onClick() }
        },
    )
}
