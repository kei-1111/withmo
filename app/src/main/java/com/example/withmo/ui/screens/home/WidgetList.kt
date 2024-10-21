package com.example.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.withmo.utils.WidgetUtils
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.collections.immutable.ImmutableList

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun WidgetList(
    widgetInfoList: ImmutableList<AppWidgetProviderInfo>,
    selectWidget: (AppWidgetProviderInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier,
    ) {
        items(widgetInfoList) { widgetInfo ->
            val appIconDrawable = remember { WidgetUtils.loadAppIcon(context, widgetInfo.provider.packageName) }
            val widgetLabel = widgetInfo.loadLabel(context.packageManager) ?: "Unknown Widget"
            val widgetDescription = widgetInfo.loadDescription(context) ?: "Unknown Widget"
            val previewDrawable = remember { WidgetUtils.loadWidgetPreviewImage(context, widgetInfo) }
            val previewLayout = remember { WidgetUtils.loadWidgetPreviewLayout(context, widgetInfo) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectWidget(widgetInfo)
                    }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (previewDrawable != null) {
                    Image(
                        painter = rememberDrawablePainter(drawable = previewDrawable),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                    )
                } else {
                    previewLayout?.asImageBitmap()?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = widgetLabel, fontWeight = FontWeight.Bold)

                    // Display app icon and package name
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (appIconDrawable != null) {
                            Image(
                                painter = rememberDrawablePainter(drawable = appIconDrawable),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = widgetDescription.toString())
                    }
                }
            }
        }
    }
}
