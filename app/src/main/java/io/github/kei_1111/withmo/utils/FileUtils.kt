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

    private const val DefaultModelFileName = "alicia_solid.vrm"

    fun fileExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    suspend fun deleteCopiedCacheFiles(context: Context) = withContext(FileIoDispatcher) {
        val cacheDir = context.cacheDir
        cacheDir.listFiles()?.forEach { file ->
            if (file.name != DefaultModelFileName) file.delete()
        }
    }

    suspend fun copyVrmFileFromUri(context: Context, uri: Uri): File? {
        val fileName = getFileName(context, uri) ?: "model.vrm"
        if (!fileName.endsWith(".vrm", ignoreCase = true)) return null

        return copyFileFromUri(context, uri, fileName)
    }

    private suspend fun copyFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        if (fileExists("${context.cacheDir}/$fileName")) return File("${context.cacheDir}/$fileName")

        return withContext(FileIoDispatcher) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
                val tempFile = File(context.cacheDir, fileName)
                FileOutputStream(tempFile).use { output -> inputStream.copyTo(output) }
                tempFile
            } catch (e: IOException) {
                Log.e(TAG, "Failed to copy file: $uri", e)
                null
            }
        }
    }

    suspend fun copyVrmFileFromAssets(context: Context): File? =
        withContext(FileIoDispatcher) {
            val outputFile = File(context.filesDir, DefaultModelFileName)
            if (outputFile.exists()) return@withContext outputFile

            try {
                context.assets.open(DefaultModelFileName).use { inputStream ->
                    FileOutputStream(outputFile).use { output -> inputStream.copyTo(output) }
                }
                outputFile
            } catch (e: IOException) {
                Log.e(TAG, "Failed to copy $DefaultModelFileName from assets", e)
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

    private const val TAG = "FileUtils"
}
