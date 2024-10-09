package com.example.withmo.domain.model

import com.unity3d.player.UnityPlayer

class ModelFile(
    val fileName: String,
    val filePath: String,
    val downloadDate: String,
) {
    fun sendPathToUnity() {
        UnityPlayer.UnitySendMessage("VRMload", "ReceiveVRMFilePath", filePath)
    }
}
