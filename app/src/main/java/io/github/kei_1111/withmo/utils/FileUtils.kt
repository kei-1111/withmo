package io.github.kei_1111.withmo.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.domain.model.ModelFile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FileUtils {
    private val FileIoDispatcher = Dispatchers.IO

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
}
