package io.github.kei_1111.withmo.core.ui.modifier

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import io.github.kei_1111.withmo.core.ui.LocalClickBlocker

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.safeClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit,
): Modifier = composed {
    val blocker = LocalClickBlocker.current
    this.combinedClickable(
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

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.safeClickable(
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit,
): Modifier = composed {
    val blocker = LocalClickBlocker.current
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
