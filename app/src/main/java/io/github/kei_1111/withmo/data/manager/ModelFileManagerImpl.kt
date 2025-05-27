package io.github.kei_1111.withmo.data.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.utils.FileUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

class ModelFileManagerImpl @Inject constructor(
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ModelFileManager {

    override suspend fun deleteCopiedCacheFiles() {
        withContext(ioDispatcher) {
            val cacheDir = context.cacheDir
            cacheDir.listFiles()?.forEach { file ->
                if (file.name != DefaultModelFileName) file.delete()
            }
        }
    }

    override suspend fun copyVrmFileFromUri(uri: Uri): File? {
        val fileName = getFileNameFromUri(uri) ?: "model.vrm"
        if (!fileName.endsWith(".vrm", ignoreCase = true)) return null

        return copyFileFromUri(uri, fileName)
    }

    private suspend fun copyFileFromUri(uri: Uri, fileName: String): File? {
        if (FileUtils.fileExists("${context.cacheDir}/$fileName")) return File("${context.cacheDir}/$fileName")

        return withContext(ioDispatcher) {
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

    override suspend fun copyVrmFileFromAssets(): File? =
        withContext(ioDispatcher) {
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

    @Suppress("MagicNumber")
    override suspend fun getVrmThumbnail(file: File): Bitmap? = withContext(Dispatchers.IO) {
        /* ---------- 1. すべてのバイトを読み込む ---------- */
        val bytes = file.readBytes()
        if (bytes.size < 20) return@withContext null // GLB ヘッダーすら無い

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        /* ---------- 2. GLB ヘッダー (12 byte) を飛ばして JSON チャンク ---------- */
        buffer.position(12)
        val jsonLen = buffer.int // chunkLength
        val jsonType = buffer.int // chunkType ('JSON')
        if (jsonType != 0x4E4F534A) return@withContext null

        val jsonBytes = ByteArray(jsonLen)
        buffer.get(jsonBytes)
        val json = JSONObject(String(jsonBytes, Charsets.UTF_8))

        /* ---------- 3. BIN チャンクの先頭位置を求める ---------- */
        val paddedJsonLen = ((jsonLen + 3) / 4) * 4 // 4byte アライン
        val binStart = 12 + 8 + paddedJsonLen + 8

        /* ---------- 4. 画像 index を取得 (VRM 0.x / 1.0 両対応) ---------- */
        val imgIdx = json.findThumbnailImageIndex() ?: return@withContext null

        /* ---------- 5. images[imgIdx] から PNG/JPEG バイト列を生成 ---------- */
        val image = json.getJSONArray("images").getJSONObject(imgIdx)

        // --- 5‑A Data URI 埋め込みの場合 -------------------------------------
        image.optString("uri")?.takeIf { it.startsWith("data:") }?.let { uri ->
            val base64Raw = uri.substringAfter(',')
            val pngBytes = Base64.decode(base64Raw, Base64.DEFAULT)
            return@withContext BitmapFactory.decodeByteArray(pngBytes, 0, pngBytes.size)
        }

        // --- 5‑B bufferView 参照の場合 -----------------------------------------
        val bvIdx = image.optInt("bufferView", -1)
        if (bvIdx >= 0) {
            val bv = json.getJSONArray("bufferViews").getJSONObject(bvIdx)
            val offset = bv.optInt("byteOffset", 0)
            val length = bv.getInt("byteLength")
            val start = binStart + offset // BIN 先頭 + オフセット
            val pngBytes = bytes.copyOfRange(start, start + length)
            return@withContext BitmapFactory.decodeByteArray(pngBytes, 0, pngBytes.size)
        }

        null
    }

    @Suppress("ReturnCount")
    private fun JSONObject.findThumbnailImageIndex(): Int? {
        val ext = optJSONObject("extensions") ?: return null

        // VRM 1.0 (VRMC_vrm)
        ext.optJSONObject("VRMC_vrm")?.let { vrm10 ->
            val ix = vrm10.getJSONObject("meta").optInt("thumbnailImage", -1)
            if (ix >= 0) return ix
        }

        // VRM 0.x (VRM)
        ext.optJSONObject("VRM")?.let { vrm0 ->
            val texIx = vrm0.getJSONObject("meta").optInt("texture", -1)
            if (texIx >= 0) {
                return getJSONArray("textures")
                    .getJSONObject(texIx)
                    .getInt("source")
            }
        }

        return null
    }

    private suspend fun getFileNameFromUri(uri: Uri): String? =
        withContext(ioDispatcher) {
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

    private companion object {
        const val DefaultModelFileName = "alicia_solid.vrm"

        const val TAG = "ModelFileManager"
    }
}
