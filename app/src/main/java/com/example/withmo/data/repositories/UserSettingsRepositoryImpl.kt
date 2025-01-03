package com.example.withmo.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.withmo.di.IoDispatcher
import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.domain.model.user_settings.AppIconShape
import com.example.withmo.domain.model.user_settings.ClockSettings
import com.example.withmo.domain.model.user_settings.ClockType
import com.example.withmo.domain.model.user_settings.NotificationSettings
import com.example.withmo.domain.model.user_settings.SideButtonSettings
import com.example.withmo.domain.model.user_settings.SortSettings
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.model.user_settings.ThemeSettings
import com.example.withmo.domain.model.user_settings.ThemeType
import com.example.withmo.domain.model.user_settings.UserSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.ui.theme.UiConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@Suppress("MaximumLineLength", "MaxLineLength")
class UserSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserSettingsRepository {
    private companion object {
        val IS_NOTIFICATION_ANIMATION_ENABLED =
            booleanPreferencesKey("is_notification_animation_enabled")
        val IS_CLOCK_SHOWN = booleanPreferencesKey("is_clock_shown")
        val CLOCK_TYPE = stringPreferencesKey("clock_type")
        val APP_ICON_SIZE = floatPreferencesKey("app_icon_size")
        val APP_ICON_SHAPE = stringPreferencesKey("app_icon_shape")
        val ROUNDED_CORNER_PERCENT = floatPreferencesKey("rounded_corner_percent")
        val IS_APP_NAME_SHOWN = booleanPreferencesKey("is_app_name_shown")
        val SORT_TYPE = stringPreferencesKey("sort_type")
        val IS_SCALE_SLIDER_BUTTON_SHOWN = booleanPreferencesKey("is_scale_slider_button_shown")
        val THEME_TYPE = stringPreferencesKey("theme_type")
    }

    override val userSettings: Flow<UserSettings> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("UseSettingsRepository", "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            UserSettings(
                notificationSettings = NotificationSettings(
                    isNotificationAnimationEnabled = preferences[IS_NOTIFICATION_ANIMATION_ENABLED]
                        ?: false,
                ),
                clockSettings = ClockSettings(
                    isClockShown = preferences[IS_CLOCK_SHOWN] ?: true,
                    clockType = preferences[CLOCK_TYPE]?.let { ClockType.valueOf(it) }
                        ?: ClockType.TOP_DATE,
                ),
                appIconSettings = AppIconSettings(
                    appIconSize = preferences[APP_ICON_SIZE] ?: UiConfig.DefaultAppIconSize,
                    appIconShape = preferences[APP_ICON_SHAPE]?.let { shape ->
                        when (shape) {
                            AppIconShape.Circle.toString() -> AppIconShape.Circle
                            AppIconShape.RoundedCorner.toString() -> AppIconShape.RoundedCorner
                            AppIconShape.Square.toString() -> AppIconShape.Square
                            else -> AppIconShape.Circle
                        }
                    } ?: AppIconShape.Circle,
                    roundedCornerPercent = preferences[ROUNDED_CORNER_PERCENT]
                        ?: UiConfig.DefaultRoundedCornerPercent,
                    isAppNameShown = preferences[IS_APP_NAME_SHOWN] ?: true,
                ),
                sortSettings = SortSettings(
                    sortType = preferences[SORT_TYPE]?.let { SortType.valueOf(it) }
                        ?: SortType.ALPHABETICAL,
                ),
                sideButtonSettings = SideButtonSettings(
                    isScaleSliderButtonShown = preferences[IS_SCALE_SLIDER_BUTTON_SHOWN] ?: true,
                ),
                themeSettings = ThemeSettings(
                    themeType = preferences[THEME_TYPE]?.let { ThemeType.valueOf(it) }
                        ?: ThemeType.TIME_BASED,
                ),
            )
        }

    override suspend fun saveSortSettings(sortSettings: SortSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SORT_TYPE] = sortSettings.sortType.name
            }
        }
    }

    override suspend fun saveNotificationSettings(notificationSettings: NotificationSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_NOTIFICATION_ANIMATION_ENABLED] =
                    notificationSettings.isNotificationAnimationEnabled
            }
        }
    }

    override suspend fun saveAppIconSettings(appIconSettings: AppIconSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[APP_ICON_SIZE] = appIconSettings.appIconSize
                preferences[APP_ICON_SHAPE] = appIconSettings.appIconShape.toString()
                preferences[ROUNDED_CORNER_PERCENT] = appIconSettings.roundedCornerPercent
                preferences[IS_APP_NAME_SHOWN] = appIconSettings.isAppNameShown
            }
        }
    }

    override suspend fun saveClockSettings(clockSettings: ClockSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_CLOCK_SHOWN] = clockSettings.isClockShown
                preferences[CLOCK_TYPE] = clockSettings.clockType.name
            }
        }
    }

    override suspend fun saveSideButtonSettings(sideButtonSettings: SideButtonSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_SCALE_SLIDER_BUTTON_SHOWN] =
                    sideButtonSettings.isScaleSliderButtonShown
            }
        }
    }

    override suspend fun saveThemeSettings(themeSettings: ThemeSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[THEME_TYPE] = themeSettings.themeType.name
            }
        }
    }
}
