package com.example.withmo.until

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.ModelFile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("NestedBlockDepth")
@RequiresApi(Build.VERSION_CODES.Q)
fun getModelFile(context: Context): ImmutableList<ModelFile> {
    val contentResolver = context.contentResolver
    val modelFileList = mutableListOf<ModelFile>()

    val cursor = try {
        contentResolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null,
        )
    } catch (e: Exception) {
        Log.e("getModelFile", "Failed to query", e)
        null
    }

    cursor?.use {
        if (it.moveToFirst()) {
            do {
                val modelFile = extractModelFileFromCursor(it)
                if (modelFile != null) {
                    modelFileList.add(modelFile)
                }
            } while (it.moveToNext())
        }
    }

    return modelFileList.toPersistentList()
}

private const val SecondsToMilliseconds = 1000L

private fun extractModelFileFromCursor(cursor: Cursor): ModelFile? {
    val fileNameIndex = cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME)
    val filePathIndex = cursor.getColumnIndex(MediaStore.Downloads.DATA)
    val downloadDateIndex = cursor.getColumnIndex(MediaStore.Downloads.DATE_ADDED)

    if (fileNameIndex == -1 || filePathIndex == -1) return null

    val fileName = cursor.getString(fileNameIndex)
    val filePath = cursor.getString(filePathIndex)
    val downloadDate = cursor.getLong(downloadDateIndex)

    return if (filePath.endsWith(".vrm")) {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

        ModelFile(
            fileName = fileName,
            filePath = filePath,
            downloadDate = sdf.format(Date(downloadDate * SecondsToMilliseconds)),
        )
    } else {
        null
    }
}
