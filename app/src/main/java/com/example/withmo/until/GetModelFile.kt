package com.example.withmo.until

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.ModelFile

@Suppress("NestedBlockDepth")
@RequiresApi(Build.VERSION_CODES.Q)
fun getModelFile(context: Context): MutableList<ModelFile> {
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

    return modelFileList
}

private fun extractModelFileFromCursor(cursor: Cursor): ModelFile? {
    val fileNameIndex = cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME)
    val filePathIndex = cursor.getColumnIndex(MediaStore.Downloads.DATA)

    if (fileNameIndex == -1 || filePathIndex == -1) return null

    val fileName = cursor.getString(fileNameIndex)
    val filePath = cursor.getString(filePathIndex)

    return if (filePath.endsWith(".vrm")) {
        ModelFile(fileName = fileName, filePath = filePath)
    } else {
        null
    }
}
