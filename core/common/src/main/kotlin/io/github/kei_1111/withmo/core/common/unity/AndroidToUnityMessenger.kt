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
    SKY_BLEND("SkyBlend"),

    VRM_LOADER("VRMloader"),

    IK_ANIMATION_CONTROLLER("IKAnimationController"),

    VRM_ANIMATION_CONTROLLER("VRMAnimationController"),

    NOTIFICATION("Notification"),
}

enum class UnityMethod(val unityName: String) {
    CHANGE_DAY("ChangeDay"),
    CHANGE_EVENING("ChangeEvening"),
    CHANGE_NIGHT("ChangeNight"),
    SET_DAY_FIXED_MODE("SetDayFixedMode"),
    SET_NIGHT_FIXED_MODE("SetNightFixedMode"),

    LOAD_VRM("LoadVRM"),

    SHOW_OBJECT("ShowObject"),

    TRIGGER_EXIT_SCREEN_ANIMATION("TriggerExitScreenAnimation"),
    TRIGGER_ENTER_SCREEN_ANIMATION("TriggerEnterScreenAnimation"),
    MOVE_LOOKAT("MoveLookat"),

    TRIGGER_TOUCH_ANIMATION("TriggerTouchAnimation"),

    ADJUST_SCALE("AdjustScale"),
}
