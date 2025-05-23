package io.github.kei_1111.withmo.utils

import java.io.File

object FileUtils {

    private const val DefaultModelFileName = "alicia_solid.vrm"

    fun fileExists(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    fun isDefaultModelFile(filePath: String): Boolean {
        val file = File(filePath)
        return file.name == DefaultModelFileName
    }
}
