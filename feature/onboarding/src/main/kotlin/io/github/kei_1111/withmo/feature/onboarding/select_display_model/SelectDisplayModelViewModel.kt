package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.usecase.GetModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.util.FileUtils
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SelectDisplayModelViewModel @Inject constructor(
    private val getModelFilePathUseCase: GetModelFilePathUseCase,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val modelFileManager: ModelFileManager,
) : StatefulBaseViewModel<SelectDisplayModelViewModelState, SelectDisplayModelState, SelectDisplayModelAction, SelectDisplayModelEffect>() {

    override fun createInitialViewModelState() = SelectDisplayModelViewModelState()
    override fun createInitialState() = SelectDisplayModelState()

    init {
        observeModelFilePath()
    }

    private fun observeModelFilePath() {
        viewModelScope.launch {
            getModelFilePathUseCase().collect { modelFilePath ->
                val isDefaultModel = modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) }
                if (isDefaultModel == false) {
                    val thumbnails = modelFilePath.path?.let { File(it) }?.let {
                        modelFileManager.getVrmThumbnail(it)
                    }
                    updateViewModelState {
                        copy(
                            modelFilePath = modelFilePath,
                            modelFileThumbnail = thumbnails,
                        )
                    }
                }
            }
        }
    }

    private fun saveModelFilePath() {
        viewModelScope.launch {
            val modelFilePath = state.value.modelFilePath
            if (modelFilePath.path != null) {
                saveModelFilePathUseCase(modelFilePath)
            } else {
                val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath
                saveModelFilePathUseCase(ModelFilePath(defaultModelFilePath))
            }
        }
    }

    override fun onAction(action: SelectDisplayModelAction) {
        when (action) {
            is SelectDisplayModelAction.OnSelectDisplayModelAreaClick -> {
                sendEffect(SelectDisplayModelEffect.OpenDocument)
            }

            is SelectDisplayModelAction.OnBackButtonClick -> {
                saveModelFilePath()
                sendEffect(SelectDisplayModelEffect.NavigateBack)
            }

            is SelectDisplayModelAction.OnNextButtonClick -> {
                saveModelFilePath()
                sendEffect(SelectDisplayModelEffect.NavigateFinish)
            }

            is SelectDisplayModelAction.OnOpenDocumentLauncherResult -> {
                viewModelScope.launch {
                    updateViewModelState { copy(isModelLoading = true) }
                    if (action.uri == null) {
                        sendEffect(SelectDisplayModelEffect.ShowToast("ファイルが選択されませんでした"))
                    } else {
                        val filePath = modelFileManager.copyVrmFileFromUri(action.uri)?.absolutePath
                        if (filePath == null) {
                            sendEffect(SelectDisplayModelEffect.ShowToast("ファイルの読み込みに失敗しました"))
                        } else {
                            val modelFilePath = ModelFilePath(filePath)
                            val thumbnails = modelFilePath.path?.let { File(it) }?.let {
                                modelFileManager.getVrmThumbnail(it)
                            }
                            updateViewModelState {
                                copy(
                                    modelFilePath = modelFilePath,
                                    modelFileThumbnail = thumbnails,
                                )
                            }
                        }
                    }
                    updateViewModelState { copy(isModelLoading = false) }
                }
            }
        }
    }

    private companion object {
        const val TAG = "SelectDisplayModelViewModel"
    }
}
