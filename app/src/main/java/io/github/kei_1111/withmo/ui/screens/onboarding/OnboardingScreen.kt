package io.github.kei_1111.withmo.ui.screens.onboarding

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.FinishContent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.SelectDisplayModelContent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.SelectFavoriteAppContent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.WelcomeContent
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing", "LongMethod")
@Composable
fun OnboardingScreen(
    onFinishButtonClick: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        scope.launch {
            viewModel.setIsModelLoading(true)
            if (uri == null) {
                showToast(context, "ファイルが選択されませんでした")
            } else {
                val filePath = viewModel.getVrmFilePath(context, uri)
                if (filePath == null) {
                    showToast(context, "ファイルの読み込みに失敗しました")
                } else {
                    viewModel.setModelFilePath(ModelFilePath(filePath))
                    viewModel.setModelFileThumbnail(ModelFilePath(filePath))
                }
            }
            viewModel.setIsModelLoading(false)
        }
    }

    val currentOnFinishButtonClick by rememberUpdatedState(onFinishButtonClick)

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is OnboardingUiEvent.OnAllAppListAppClick -> {
                    viewModel.addSelectedAppList(event.appInfo)
                }

                is OnboardingUiEvent.OnFavoriteAppListAppClick -> {
                    viewModel.removeSelectedAppList(event.appInfo)
                }

                is OnboardingUiEvent.OnAppSearchQueryChange -> {
                    viewModel.onValueChangeAppSearchQuery(event.query)
                }

                is OnboardingUiEvent.OnSelectDisplayModelAreaClick -> {
                    openDocumentLauncher.launch(arrayOf("*/*"))
                }

                is OnboardingUiEvent.OnNextButtonClick -> {
                    viewModel.navigateToNextPage(
                        onFinish = {
                            viewModel.saveSetting(context)
                            currentOnFinishButtonClick()
                        },
                    )
                }

                is OnboardingUiEvent.OnPreviousButtonClick -> {
                    viewModel.navigateToPreviousPage()
                }
            }
        }.launchIn(this)
    }

    OnboardingScreen(
        appList = appList.toPersistentList(),
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize(),
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun OnboardingScreen(
    appList: ImmutableList<AppInfo>,
    uiState: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    Surface(
        modifier = modifier,
    ) {
        when (uiState.currentPage) {
            OnboardingPage.Welcome -> {
                WelcomeContent(
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.SelectFavoriteApp -> {
                SelectFavoriteAppContent(
                    appList = appList,
                    uiState = uiState,
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.SelectDisplayModel -> {
                SelectDisplayModelContent(
                    uiState = uiState,
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.Finish -> {
                FinishContent(
                    onEvent = onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
        }
    }
}
