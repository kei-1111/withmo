package io.github.kei_1111.withmo.ui.screens.home

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.LabelMediumText
import io.github.kei_1111.withmo.ui.theme.UiConfig
import io.github.kei_1111.withmo.utils.WidgetUtils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WidgetList(
    groupedWidgetInfoMap: ImmutableMap<String, List<AppWidgetProviderInfo>>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
        contentPadding = PaddingValues(UiConfig.MediumPadding),
    ) {
        groupedWidgetInfoMap.forEach { (packageName, widgetInfoList) ->
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
fun WidgetContainer(
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
                    .height(UiConfig.SettingItemHeight)
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = UiConfig.MediumPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                appIcon?.let {
                    Image(
                        painter = rememberDrawablePainter(drawable = appIcon),
                        contentDescription = null,
                        modifier = Modifier.size(UiConfig.SettingsScreenItemIconSize),
                    )
                }
                Spacer(
                    modifier = Modifier.width(UiConfig.ExtraSmallPadding),
                )
                appLabel?.let {
                    BodyMediumText(
                        text = appLabel,
                        modifier = Modifier.weight(UiConfig.DefaultWeight),
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
                        .padding(UiConfig.MediumPadding)
                        .align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
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

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WidgetItem(
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
                    .sizeIn(maxWidth = UiConfig.WidgetPreviewSize, maxHeight = UiConfig.WidgetPreviewSize),
            )
        } else {
            previewLayout?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier
                        .sizeIn(maxWidth = UiConfig.WidgetPreviewSize, maxHeight = UiConfig.WidgetPreviewSize),
                )
            }
        }
        Spacer(
            modifier = Modifier.height(UiConfig.TinyPadding),
        )
        widgetLabel?.let {
            BodyMediumText(text = it)
        }
        widgetDescription?.let {
            LabelMediumText(
                text = it.toString(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = UiConfig.DisabledContentAlpha),
                maxLines = UiConfig.WidgetDescriptionMaxLines,
            )
        }
    }
}
