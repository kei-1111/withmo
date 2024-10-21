package com.example.withmo.utils

import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi

object WidgetUtils {
    fun loadWidgetPreviewImage(context: Context, info: AppWidgetProviderInfo): Drawable? {
        return try {
            info.loadPreviewImage(context, 0)
        } catch (e: Exception) {
            Log.e("loadWidgetPreview", "Failed to load widget preview image", e)
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun loadWidgetPreviewLayout(context: Context, info: AppWidgetProviderInfo): Bitmap? {
        return try {
            // ウィジェット提供元のパッケージコンテキストを取得
            val packageContext = context.createPackageContext(
                info.provider.packageName,
                PackageContextFlags,
            )

            // プレビュー用のレイアウトリソースIDを取得
            val layoutId = info.previewLayout

            if (layoutId == 0) {
                // プレビュー用レイアウトが提供されていない場合
                return null
            }

            // レイアウトをインフレート
            val inflater = LayoutInflater.from(packageContext)
            val view = inflater.inflate(layoutId, null)

            // ビューのサイズを測定してレイアウト
            val widthSpec = View.MeasureSpec.makeMeasureSpec(PreviewImageSize, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(PreviewImageSize, View.MeasureSpec.AT_MOST)
            view.measure(widthSpec, heightSpec)
            view.layout(0, 0, view.measuredWidth, view.measuredHeight)

            // ビューをビットマップに描画
            val bitmap = Bitmap.createBitmap(
                view.measuredWidth,
                view.measuredHeight,
                Bitmap.Config.ARGB_8888,
            )
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            bitmap
        } catch (e: Exception) {
            Log.e("generateWidgetPreview", "Failed to generate widget preview", e)
            null
        }
    }

    fun loadAppIcon(context: Context, packageName: String): Drawable? {
        return try {
            val applicationInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationIcon(applicationInfo)
        } catch (e: Exception) {
            Log.e("loadAppIcon", "Failed to load app icon", e)
            null
        }
    }

    private const val PreviewImageSize = 500
    private const val PackageContextFlags = Context.CONTEXT_IGNORE_SECURITY or Context.CONTEXT_INCLUDE_CODE
}
