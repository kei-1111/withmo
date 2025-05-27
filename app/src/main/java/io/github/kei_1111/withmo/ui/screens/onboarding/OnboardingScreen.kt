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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.FinishContent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.SelectDisplayModelContent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.SelectFavoriteAppContent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.contents.WelcomeContent
import io.github.kei_1111.withmo.utils.showToast
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing", "LongMethod")
@Composable
fun OnboardingScreen(
    onFinishButtonClick: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val appList by viewModel.appList.collectAsStateWithLifecycle()

    val openDocumentLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        viewModel.onAction(OnboardingAction.OnOpenDocumentLauncherResult(uri))
    }

    val currentOnFinishButtonClick by rememberUpdatedState(onFinishButtonClick)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OnboardingEffect.OpenDocument -> openDocumentLauncher.launch(arrayOf("*/*"))

                is OnboardingEffect.NavigateHome -> currentOnFinishButtonClick()

                is OnboardingEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    OnboardingScreen(
        appList = appList.toPersistentList(),
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
private fun OnboardingScreen(
    appList: ImmutableList<AppInfo>,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val targetBottomPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val bottomPaddingValue by animateDpAsState(targetValue = targetBottomPadding)

    Surface(
        modifier = modifier,
    ) {
        when (state.currentPage) {
            OnboardingPage.Welcome -> {
                WelcomeContent(
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.SelectFavoriteApp -> {
                SelectFavoriteAppContent(
                    appList = appList,
                    state = state,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.SelectDisplayModel -> {
                SelectDisplayModelContent(
                    state = state,
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
            OnboardingPage.Finish -> {
                FinishContent(
                    onAction = onAction,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = bottomPaddingValue),
                )
            }
        }
    }
}
