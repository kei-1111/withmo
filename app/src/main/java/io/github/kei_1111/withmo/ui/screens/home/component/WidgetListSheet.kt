package io.github.kei_1111.withmo.ui.screens.home.component

import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.ui.LocalAppWidgetManager
import io.github.kei_1111.withmo.core.util.WidgetUtils
import io.github.kei_1111.withmo.ui.screens.home.HomeAction
import io.github.kei_1111.withmo.ui.screens.home.HomeScreenDimensions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WidgetListSheet(
    widgetListSheetState: SheetState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val appWidgetManager = LocalAppWidgetManager.current
    val groupedWidgetInfoMaps = appWidgetManager
        .installedProviders
        .groupBy { it.provider.packageName }
        .toPersistentMap()

    ModalBottomSheet(
        onDismissRequest = { onAction(HomeAction.OnWidgetListSheetSwipeDown) },
        sheetState = widgetListSheetState,
        modifier = modifier,
    ) {
        WidgetList(
            groupedWidgetInfoMaps = groupedWidgetInfoMaps,
            selectWidget = { onAction(HomeAction.OnWidgetListSheetItemClick(it)) },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WidgetList(
    groupedWidgetInfoMaps: ImmutableMap<String, List<AppWidgetProviderInfo>>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
        contentPadding = PaddingValues(Paddings.Medium),
    ) {
        groupedWidgetInfoMaps.forEach { (packageName, widgetInfoList) ->
            item {
                WidgetContainer(
                    packageName = packageName,
                    widgetInfoList = widgetInfoList.toPersistentList(),
                    selectWidget = selectWidget,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Suppress("LongMethod")
@Composable
private fun WidgetContainer(
    packageName: String,
    widgetInfoList: ImmutableList<AppWidgetProviderInfo>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val appIcon = remember { WidgetUtils.loadAppIcon(context, packageName) }
    val appLabel = remember { WidgetUtils.loadAppLabel(context, packageName) }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .height(CommonDimensions.SettingItemHeight)
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = Paddings.Medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                appIcon?.let {
                    Image(
                        painter = rememberDrawablePainter(drawable = appIcon),
                        contentDescription = null,
                        modifier = Modifier.size(IconSizes.Medium),
                    )
                }
                Spacer(
                    modifier = Modifier.width(Paddings.ExtraSmall),
                )
                appLabel?.let {
                    BodyMediumText(
                        text = appLabel,
                        modifier = Modifier.weight(Weights.Medium),
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                    contentDescription = if (expanded) "Close" else "Open",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(Paddings.Medium)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    widgetInfoList.forEach { widgetInfo ->
                        WidgetItem(
                            widgetInfo = widgetInfo,
                            selectWidget = selectWidget,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

private const val WidgetDescriptionMaxLines = 3

@RequiresApi(Build.VERSION_CODES.S)
@Composable
private fun WidgetItem(
    widgetInfo: AppWidgetProviderInfo,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val widgetLabel = widgetInfo.loadLabel(context.packageManager)
    val widgetDescription = widgetInfo.loadDescription(context)
    val previewDrawable = remember { WidgetUtils.loadWidgetPreviewImage(context, widgetInfo) }
    val previewLayout = remember { WidgetUtils.loadWidgetPreviewLayout(context, widgetInfo) }

    Column(
        modifier = modifier
            .clickable { selectWidget(widgetInfo) },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (previewDrawable != null) {
            Image(
                painter = rememberDrawablePainter(drawable = previewDrawable),
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(
                        maxWidth = HomeScreenDimensions.WidgetPreviewSize,
                        maxHeight = HomeScreenDimensions.WidgetPreviewSize,
                    ),
            )
        } else {
            previewLayout?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(
                            maxWidth = HomeScreenDimensions.WidgetPreviewSize,
                            maxHeight = HomeScreenDimensions.WidgetPreviewSize,
                        ),
                )
            }
        }
        Spacer(
            modifier = Modifier.height(Paddings.Tiny),
        )
        widgetLabel?.let {
            BodyMediumText(text = it)
        }
        widgetDescription?.let {
            LabelMediumText(
                text = it.toString(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled),
                maxLines = WidgetDescriptionMaxLines,
            )
        }
    }
}
