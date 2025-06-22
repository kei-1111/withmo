package io.github.kei_1111.withmo.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.di.UserSetting
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@Suppress("MaximumLineLength", "MaxLineLength")
class UserSettingsRepositoryImpl @Inject constructor(
    @UserSetting private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserSettingsRepository {

    override val userSettings: Flow<UserSettings> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
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
                    isNotificationBadgeEnabled = preferences[IS_NOTIFICATION_BADGE_ENABLED]
                        ?: false,
                ),
                clockSettings = ClockSettings(
                    isClockShown = preferences[IS_CLOCK_SHOWN] ?: true,
                    clockType = preferences[CLOCK_TYPE]?.let { ClockType.valueOf(it) }
                        ?: ClockType.TOP_DATE,
                ),
                appIconSettings = AppIconSettings(
                    appIconShape = preferences[APP_ICON_SHAPE]?.let { shape ->
                        when (shape) {
                            AppIconShape.Circle.toString() -> AppIconShape.Circle
                            AppIconShape.RoundedCorner.toString() -> AppIconShape.RoundedCorner
                            AppIconShape.Square.toString() -> AppIconShape.Square
                            else -> AppIconShape.Circle
                        }
                    } ?: AppIconShape.Circle,
                    roundedCornerPercent = preferences[ROUNDED_CORNER_PERCENT] ?: AppConstants.DefaultRoundedCornerPercent,
                ),
                sortSettings = SortSettings(
                    sortType = preferences[SORT_TYPE]?.let { SortType.valueOf(it) }
                        ?: SortType.ALPHABETICAL,
                ),
                sideButtonSettings = SideButtonSettings(
                    isShowScaleSliderButtonShown = preferences[IS_SHOW_SCALE_SLIDER_BUTTON_SHOWN] ?: true,
                    isOpenDocumentButtonShown = preferences[IS_OPEN_DOCUMENT_BUTTON_SHOWN] ?: true,
                    isSetDefaultModelButtonShown = preferences[IS_SET_DEFAULT_MODEL_BUTTON_SHOWN] ?: true,
                    isNavigateSettingsButtonShown = preferences[IS_NAVIGATE_SETTINGS_BUTTON_SHOWN] ?: true,
                ),
                themeSettings = ThemeSettings(
                    themeType = preferences[THEME_TYPE]?.let { ThemeType.valueOf(it) }
                        ?: ThemeType.TIME_BASED,
                ),
                modelFilePath = ModelFilePath(
                    path = preferences[MODEL_FILE_PATH],
                ),
                modelSettings = ModelSettings(
                    scale = preferences[SCALE] ?: AppConstants.DefaultModelScale,
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
                preferences[IS_NOTIFICATION_BADGE_ENABLED] =
                    notificationSettings.isNotificationBadgeEnabled
            }
        }
    }

    override suspend fun saveAppIconSettings(appIconSettings: AppIconSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[APP_ICON_SHAPE] = appIconSettings.appIconShape.toString()
                preferences[ROUNDED_CORNER_PERCENT] = appIconSettings.roundedCornerPercent
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
                preferences[IS_SHOW_SCALE_SLIDER_BUTTON_SHOWN] =
                    sideButtonSettings.isShowScaleSliderButtonShown
                preferences[IS_OPEN_DOCUMENT_BUTTON_SHOWN] =
                    sideButtonSettings.isOpenDocumentButtonShown
                preferences[IS_SET_DEFAULT_MODEL_BUTTON_SHOWN] =
                    sideButtonSettings.isSetDefaultModelButtonShown
                preferences[IS_NAVIGATE_SETTINGS_BUTTON_SHOWN] =
                    sideButtonSettings.isNavigateSettingsButtonShown
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

    override suspend fun saveModelFilePath(modelFilePath: ModelFilePath) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                val path = modelFilePath.path
                if (path != null) {
                    preferences[MODEL_FILE_PATH] = path
                } else {
                    preferences.remove(MODEL_FILE_PATH)
                }
            }
        }
    }

    override suspend fun saveModelSettings(modelSettings: ModelSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SCALE] = modelSettings.scale
            }
        }
    }

    private companion object {
        val IS_NOTIFICATION_ANIMATION_ENABLED =
            booleanPreferencesKey("is_notification_animation_enabled")
        val IS_NOTIFICATION_BADGE_ENABLED =
            booleanPreferencesKey("is_notification_badge_enabled")
        val IS_CLOCK_SHOWN = booleanPreferencesKey("is_clock_shown")
        val CLOCK_TYPE = stringPreferencesKey("clock_type")
        val APP_ICON_SIZE = floatPreferencesKey("app_icon_size")
        val APP_ICON_SHAPE = stringPreferencesKey("app_icon_shape")
        val ROUNDED_CORNER_PERCENT = floatPreferencesKey("rounded_corner_percent")
        val IS_APP_NAME_SHOWN = booleanPreferencesKey("is_app_name_shown")
        val IS_FAVORITE_APP_BACKGROUND_SHOWN = booleanPreferencesKey("is_favorite_app_background_shown")
        val SORT_TYPE = stringPreferencesKey("sort_type")
        val IS_SHOW_SCALE_SLIDER_BUTTON_SHOWN = booleanPreferencesKey("is_show_scale_slider_button_shown")
        val IS_OPEN_DOCUMENT_BUTTON_SHOWN = booleanPreferencesKey("is_open_document_button_shown")
        val IS_SET_DEFAULT_MODEL_BUTTON_SHOWN = booleanPreferencesKey("is_set_default_model_button_shown")
        val IS_NAVIGATE_SETTINGS_BUTTON_SHOWN = booleanPreferencesKey("is_navigate_settings_button_shown")
        val THEME_TYPE = stringPreferencesKey("theme_type")
        val MODEL_FILE_PATH = stringPreferencesKey("model_file_path")
        val SCALE = floatPreferencesKey("scale")

        const val TAG = "UserSettingsRepository"
    }
}
