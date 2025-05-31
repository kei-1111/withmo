package io.github.kei_1111.withmo.core.common

data object IntentConstants {
    data object Action {
        const val StartActivity = "start_activity"
        const val NotificationReceived = "notification_received"
    }

    data object ExtraKey {
        const val PackageName = "package_name"
    }
}
