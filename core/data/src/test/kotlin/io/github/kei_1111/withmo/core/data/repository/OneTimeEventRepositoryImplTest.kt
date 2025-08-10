package io.github.kei_1111.withmo.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesOf
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OneTimeEventRepositoryImplTest {

    private lateinit var mockDataStore: DataStore<Preferences>
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        mockDataStore = mockk()
        testDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `オンボーディング初期状態でfalseを取得できること`() = runTest(testDispatcher) {
        every { mockDataStore.data } returns flowOf(emptyPreferences())
        
        val repository = OneTimeEventRepositoryImpl(mockDataStore, testDispatcher)

        repository.isOnboardingFirstShown.test {
            val result = awaitItem()
            assertEquals(false, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `モデル変更警告初期状態でfalseを取得できること`() = runTest(testDispatcher) {
        every { mockDataStore.data } returns flowOf(emptyPreferences())
        
        val repository = OneTimeEventRepositoryImpl(mockDataStore, testDispatcher)

        repository.isModelChangeWarningFirstShown.test {
            val result = awaitItem()
            assertEquals(false, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `オンボーディング表示済み状態でtrueを取得できること`() = runTest(testDispatcher) {
        val isOnboardingFirstShownKey = booleanPreferencesKey("is_onboarding_first_shown")
        val preferences = preferencesOf(isOnboardingFirstShownKey to true)
        every { mockDataStore.data } returns flowOf(preferences)
        
        val repository = OneTimeEventRepositoryImpl(mockDataStore, testDispatcher)

        repository.isOnboardingFirstShown.test {
            val result = awaitItem()
            assertEquals(true, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `オンボーディング表示済みをマークできること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = preferencesOf(booleanPreferencesKey("is_onboarding_first_shown") to true)
        
        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences
        
        val repository = OneTimeEventRepositoryImpl(mockDataStore, testDispatcher)

        repository.markOnboardingFirstShown()

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }


    @Test
    fun `モデル変更警告表示済みをマークできること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = preferencesOf(booleanPreferencesKey("is_model_change_warning_first_shown") to true)
        
        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences
        
        val repository = OneTimeEventRepositoryImpl(mockDataStore, testDispatcher)

        repository.markModelChangeWarningFirstShown()

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }
}