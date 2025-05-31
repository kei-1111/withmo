package io.github.kei_1111.withmo.core.common.unity

import com.unity3d.player.UnityPlayer

object AndroidToUnityMessenger {
    fun sendMessage(
        unityObject: UnityObject,
        unityMethod: UnityMethod,
        message: String,
    ) {
        UnityPlayer.UnitySendMessage(unityObject.unityName, unityMethod.unityName, message)
    }
}

enum class UnityObject(val unityName: String) {
    SkyBlend("SkyBlend"),

    VRMloader("VRMloader"),

    SliderManeger("SliderManeger"),

    IKAnimationController("IKAnimationController"),

    VRMAnimationController("VRMAnimationController"),

    Notification("Notification"),
}

enum class UnityMethod(val unityName: String) {
    ChangeDay("ChangeDay"),
    ChangeEvening("ChangeEvening"),
    ChangeNight("ChangeNight"),
    SetDayFixedMode("SetDayFixedMode"),
    SetNightFixedMode("SetNightFixedMode"),

    LoadVRM("LoadVRM"),

    ShowObject("ShowObject"),
    HideObject("HideObject"),

    TriggerExitScreenAnimation("TriggerExitScreenAnimation"),
    TriggerEnterScreenAnimation("TriggerEnterScreenAnimation"),
    MoveLookat("MoveLookat"),

    TriggerTouchAnimation("TriggerTouchAnimation"),
}
