package com.example.withmo.domain.model

import com.unity3d.player.UnityPlayer

data class ModelFile(
    val fileName: String,
    val filePath: String,
) {
    fun sendPathToUnity() {
        UnityPlayer.UnitySendMessage("VRMload", "ReceiveVRMFilePath", filePath)
    }
}
