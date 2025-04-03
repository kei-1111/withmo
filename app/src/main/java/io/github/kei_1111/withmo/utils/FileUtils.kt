package io.github.kei_1111.withmo.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    private val FileIoDispatcher = Dispatchers.IO

    fun fileExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    suspend fun deleteAllCacheFiles(context: Context) = withContext(FileIoDispatcher) {
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { it.delete() }
    }

    suspend fun copyVrmFile(context: Context, uri: Uri): File? {
        val fileName = getFileName(context, uri) ?: "model.vrm"
        if (!fileName.endsWith(".vrm", ignoreCase = true)) return null

        return copyFile(context, uri, fileName)
    }

    private suspend fun copyFile(context: Context, uri: Uri, fileName: String): File? =
        withContext(FileIoDispatcher) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
                val tempFile = File(context.cacheDir, fileName)
                FileOutputStream(tempFile).use { output -> inputStream.copyTo(output) }
                tempFile
            } catch (e: IOException) {
                Log.e("copyFile", "Failed to copy file: $uri", e)
                null
            }
        }

    private suspend fun getFileName(context: Context, uri: Uri): String? =
        withContext(FileIoDispatcher) {
            if (uri.scheme == "content") {
                context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                        return@withContext cursor.getString(nameIndex)
                    }
                }
            }
            return@withContext uri.lastPathSegment?.substringAfterLast('/')
        }
}
