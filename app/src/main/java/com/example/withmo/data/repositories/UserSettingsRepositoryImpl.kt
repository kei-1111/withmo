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
import com.example.withmo.domain.model.ClockMode
import com.example.withmo.domain.model.SortMode
import com.example.withmo.domain.model.UserSettings
import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.ui.theme.UiConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class UserSettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserSettingsRepository {
    private companion object {
        val SHOW_CLOCK = booleanPreferencesKey("show_clock")
        val CLOCK_MODE = stringPreferencesKey("clock_mode")
        val SHOW_NOTIFICATION_ANIMATION = booleanPreferencesKey("show_notification_animation")
        val MODEL_FILE_LIST = stringPreferencesKey("model_file_list")
        val APP_ICON_SIZE = floatPreferencesKey("app_icon_size")
        val APP_ICON_PADDING = floatPreferencesKey("app_icon_padding")
        val SHOW_APP_NAME = booleanPreferencesKey("show_app_name")
        val SORT_MODE = stringPreferencesKey("sort_mode")
        val SHOW_SCALE_SLIDER_BUTTON = booleanPreferencesKey("show_scale_slider_button")
        val SHOW_SORT_BUTTON = booleanPreferencesKey("show_sort_button")

        const val TAG = "UserPreferencesRepo"
    }

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
                showClock = preferences[SHOW_CLOCK] ?: true,
                clockMode = preferences[CLOCK_MODE]?.let { ClockMode.valueOf(it) }
                    ?: ClockMode.TOP_DATE,
                showNotificationAnimation = preferences[SHOW_NOTIFICATION_ANIMATION] ?: false,
                modelFileList = mutableListOf(),
                appIconSize = preferences[APP_ICON_SIZE] ?: UiConfig.DefaultAppIconSize,
                appIconPadding = preferences[APP_ICON_PADDING] ?: UiConfig.DefaultAppIconPadding,
                showAppName = preferences[SHOW_APP_NAME] ?: true,
                sortMode = preferences[SORT_MODE]?.let { SortMode.valueOf(it) }
                    ?: SortMode.ALPHABETICAL,
                showScaleSliderButton = preferences[SHOW_SCALE_SLIDER_BUTTON] ?: true,
                showSortButton = preferences[SHOW_SORT_BUTTON] ?: true,
            )
        }

    override suspend fun saveSortMode(sortMode: SortMode) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SORT_MODE] = sortMode.name
            }
        }
    }

    override suspend fun saveUserSetting(userSettings: UserSettings) {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SHOW_CLOCK] = userSettings.showClock
                preferences[CLOCK_MODE] = userSettings.clockMode.name
                preferences[SHOW_NOTIFICATION_ANIMATION] = userSettings.showNotificationAnimation
                preferences[APP_ICON_SIZE] = userSettings.appIconSize
                preferences[APP_ICON_PADDING] = userSettings.appIconPadding
                preferences[SHOW_APP_NAME] = userSettings.showAppName
                preferences[SORT_MODE] = userSettings.sortMode.name
                preferences[SHOW_SCALE_SLIDER_BUTTON] = userSettings.showScaleSliderButton
                preferences[SHOW_SORT_BUTTON] = userSettings.showSortButton
            }
        }
    }
}