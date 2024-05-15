package com.example.withmo.until

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.ModelFile

@RequiresApi(Build.VERSION_CODES.Q)
fun getModelFile(context: Context): MutableList<ModelFile> {
    val contentResolver = context.contentResolver
    var cursor: Cursor? = null
    val modelFileList = mutableListOf<ModelFile>()

    // 例外を受け取る
    try {
        cursor = contentResolver.query(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val fileNameIndex = cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME)
                val filePathIndex = cursor.getColumnIndex(MediaStore.Downloads.DATA)
                val filePath = if (fileNameIndex != -1) {
                    cursor.getString(fileNameIndex)
                } else {
                    ""
                }
                if (filePath.endsWith(".vrm")) {
                    modelFileList.add(
                        ModelFile(
                            fileName = filePath,
                            filePath = if (fileNameIndex != -1) {
                                cursor.getString(filePathIndex)
                            } else {
                                ""
                            }
                        )
                    )
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (cursor != null) {
            cursor.close()
        }
    }

    return modelFileList
}