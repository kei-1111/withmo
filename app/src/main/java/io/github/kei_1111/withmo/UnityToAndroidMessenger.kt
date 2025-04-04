package io.github.kei_1111.withmo

import java.lang.ref.WeakReference

object UnityToAndroidMessenger {
    interface MessageReceiverFromUnity {
        fun onMessageReceivedFromUnity(message: String)
    }
    var receiver: WeakReference<MessageReceiverFromUnity>? = null

    fun sendMessage(message: String) {
        receiver?.get()?.onMessageReceivedFromUnity(message)
    }
}
