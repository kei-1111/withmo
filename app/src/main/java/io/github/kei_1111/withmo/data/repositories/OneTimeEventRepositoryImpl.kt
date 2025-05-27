package io.github.kei_1111.withmo.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.di.OneTimeEvent
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class OneTimeEventRepositoryImpl @Inject constructor(
    @OneTimeEvent private val dataStore: DataStore<Preferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : OneTimeEventRepository {

    override val isOnboardingFirstShown: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading is_onboarding_first_shown preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_ONBOARDING_FIRST_SHOWN] ?: false
        }

    override val isModelChangeWarningFirstShown: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading is_model_change_warning_first_shown preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[IS_MODEL_CHANGE_WARNING_FIRST_SHOWN] ?: false
        }

    override suspend fun markOnboardingFirstShown() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_ONBOARDING_FIRST_SHOWN] = true
            }
        }
    }

    override suspend fun markModelChangeWarningFirstShown() {
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[IS_MODEL_CHANGE_WARNING_FIRST_SHOWN] = true
            }
        }
    }

    private companion object {
        val IS_ONBOARDING_FIRST_SHOWN = booleanPreferencesKey("is_onboarding_first_shown")
        val IS_MODEL_CHANGE_WARNING_FIRST_SHOWN =
            booleanPreferencesKey("is_model_change_warning_first_shown")

        const val TAG = "OneTimeEventRepository"
    }
}
