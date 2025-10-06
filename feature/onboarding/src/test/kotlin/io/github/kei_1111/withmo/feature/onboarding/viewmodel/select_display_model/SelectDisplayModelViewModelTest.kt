package io.github.kei_1111.withmo.feature.onboarding.viewmodel.select_display_model

import android.graphics.Bitmap
import android.net.Uri
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.usecase.GetModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model.SelectDisplayModelAction
import io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model.SelectDisplayModelEffect
import io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model.SelectDisplayModelState
import io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model.SelectDisplayModelViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SelectDisplayModelViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getModelFilePathUseCase: GetModelFilePathUseCase
    private lateinit var saveModelFilePathUseCase: SaveModelFilePathUseCase
    private lateinit var modelFileManager: ModelFileManager
    private lateinit var viewModel: SelectDisplayModelViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getModelFilePathUseCase = mockk()
        saveModelFilePathUseCase = mockk()
        modelFileManager = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `初期状態でモデルファイルパスを取得して表示する`() = runTest {
        val modelFilePath = ModelFilePath("/path/to/model.vrm")
        val thumbnail = mockk<Bitmap>()
        val file = mockk<File>()

        every { file.name } returns "model.vrm"
        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(modelFilePath))
        coEvery { modelFileManager.getVrmThumbnail(any()) } returns thumbnail

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.state.test {
            assertEquals(SelectDisplayModelState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is SelectDisplayModelState.Stable)
        }
    }

    @Test
    fun `OnSelectDisplayModelAreaClickアクションでOpenDocumentエフェクトが送信される`() = runTest {
        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(ModelFilePath(null)))

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.effect.test {
            viewModel.onAction(SelectDisplayModelAction.OnSelectDisplayModelAreaClick)

            val effect = awaitItem()
            assertEquals(SelectDisplayModelEffect.OpenDocument, effect)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでモデルが保存されNavigateBackエフェクトが送信される`() = runTest {
        val modelFilePath = ModelFilePath("/path/to/model.vrm")
        val thumbnail = mockk<Bitmap>()
        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(modelFilePath))
        coEvery { saveModelFilePathUseCase(any()) } returns Unit
        coEvery { modelFileManager.getVrmThumbnail(any()) } returns thumbnail

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.state.test {
            assertEquals(SelectDisplayModelState.Idle, awaitItem())
            awaitItem()

            viewModel.effect.test {
                viewModel.onAction(SelectDisplayModelAction.OnBackButtonClick)

                val effect = awaitItem()
                assertEquals(SelectDisplayModelEffect.NavigateBack, effect)

                advanceUntilIdle()

                coVerify { saveModelFilePathUseCase(modelFilePath) }
            }
        }
    }

    @Test
    fun `OnNextButtonClickアクションでモデルが保存されNavigateFinishエフェクトが送信される`() = runTest {
        val modelFilePath = ModelFilePath("/path/to/model.vrm")
        val thumbnail = mockk<Bitmap>()
        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(modelFilePath))
        coEvery { saveModelFilePathUseCase(any()) } returns Unit
        coEvery { modelFileManager.getVrmThumbnail(any()) } returns thumbnail

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.state.test {
            assertEquals(SelectDisplayModelState.Idle, awaitItem())
            awaitItem()

            viewModel.effect.test {
                viewModel.onAction(SelectDisplayModelAction.OnNextButtonClick)

                val effect = awaitItem()
                assertEquals(SelectDisplayModelEffect.NavigateFinish, effect)

                advanceUntilIdle()

                coVerify { saveModelFilePathUseCase(modelFilePath) }
            }
        }
    }

    @Test
    fun `OnOpenDocumentLauncherResultアクションでnullの場合エラートーストが表示される`() = runTest {
        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(ModelFilePath(null)))

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.effect.test {
            viewModel.onAction(SelectDisplayModelAction.OnOpenDocumentLauncherResult(null))

            val effect = awaitItem()
            assertTrue(effect is SelectDisplayModelEffect.ShowToast)
            assertEquals("ファイルが選択されませんでした", (effect as SelectDisplayModelEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnOpenDocumentLauncherResultアクションで有効なURIの場合モデルが更新される`() = runTest {
        val uri = mockk<Uri>()
        val copiedFile = mockk<File>()
        val thumbnail = mockk<Bitmap>()

        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(ModelFilePath(null)))
        every { copiedFile.absolutePath } returns "/path/to/copied.vrm"
        coEvery { modelFileManager.copyVrmFileFromUri(uri) } returns copiedFile
        coEvery { modelFileManager.getVrmThumbnail(any()) } returns thumbnail

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.onAction(SelectDisplayModelAction.OnOpenDocumentLauncherResult(uri))

        advanceUntilIdle()

        coVerify { modelFileManager.copyVrmFileFromUri(uri) }
    }

    @Test
    fun `OnOpenDocumentLauncherResultアクションでファイルコピーが失敗した場合エラートーストが表示される`() = runTest {
        val uri = mockk<Uri>()

        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(ModelFilePath(null)))
        coEvery { modelFileManager.copyVrmFileFromUri(uri) } returns null

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.effect.test {
            viewModel.onAction(SelectDisplayModelAction.OnOpenDocumentLauncherResult(uri))

            val effect = awaitItem()
            assertTrue(effect is SelectDisplayModelEffect.ShowToast)
            assertEquals("ファイルの読み込みに失敗しました", (effect as SelectDisplayModelEffect.ShowToast).message)
        }
    }

    @Test
    fun `モデルパスがnullの場合デフォルトモデルが保存される`() = runTest {
        val defaultFile = mockk<File>()
        val defaultPath = "/path/to/default.vrm"

        coEvery { getModelFilePathUseCase() } returns flowOf(Result.success(ModelFilePath(null)))
        every { defaultFile.absolutePath } returns defaultPath
        coEvery { modelFileManager.copyVrmFileFromAssets() } returns defaultFile
        coEvery { saveModelFilePathUseCase(any()) } returns Unit

        viewModel = SelectDisplayModelViewModel(
            getModelFilePathUseCase,
            saveModelFilePathUseCase,
            modelFileManager,
        )

        viewModel.effect.test {
            viewModel.onAction(SelectDisplayModelAction.OnNextButtonClick)

            val effect = awaitItem()
            assertEquals(SelectDisplayModelEffect.NavigateFinish, effect)

            advanceUntilIdle()

            coVerify { modelFileManager.copyVrmFileFromAssets() }
            coVerify { saveModelFilePathUseCase(ModelFilePath(defaultPath)) }
        }
    }
}
