package io.github.kei_1111.withmo.core.util

import java.io.File

object FileUtils {

    private const val DEFAULT_MODEL_FILE_NAME = "alicia_solid.vrm"

    fun fileExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    fun isDefaultModelFile(filePath: String): Boolean {
        val file = File(filePath)
        return file.name == DEFAULT_MODEL_FILE_NAME
    }
}
