package io.github.kei_1111.withmo.domain.manager

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface ModelFileManager {
    suspend fun deleteCopiedCacheFiles()

    suspend fun copyVrmFileFromUri(uri: Uri): File?

    suspend fun copyVrmFileFromAssets(): File?

    suspend fun getVrmThumbnail(file: File): Bitmap?
}
