package com.example.withmo.ui.screens.notification_settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.flowWithLifecycle
import com.example.withmo.domain.model.Screen
import com.example.withmo.ui.component.WithmoTopAppBar
import com.example.withmo.until.showToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Suppress("ModifierMissing", "LongMethod")
@Composable
fun NotificationSettingsScreen(
    navigateToSettingsScreen: () -> Unit,
    viewModel: NotificationSettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val latestNavigateToSettingsScreen by rememberUpdatedState(navigateToSettingsScreen)

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = {
                if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)) {
                    viewModel.changeIsNotificationAnimationEnable(true)
                } else {
                    viewModel.changeIsNotificationAnimationEnable(false)
                }
            },
        )

    LaunchedEffect(lifecycleOwner, viewModel) {
        viewModel.uiEvent.flowWithLifecycle(lifecycleOwner.lifecycle).onEach { event ->
            when (event) {
                is NotificationSettingsUiEvent.ChangeIsNotificationAnimationEnabled -> {
                    if (NotificationManagerCompat.getEnabledListenerPackages(context).contains(context.packageName)) {
                        viewModel.changeIsNotificationAnimationEnable(event.isNotificationAnimationEnabled)
                    } else {
                        viewModel.changeIsNotificationAnimationEnable(false)
                        viewModel.changeIsNotificationPermissionDialogShown(true)
                    }
                }

                is NotificationSettingsUiEvent.NotificationPermissionDialogOnConfirm -> {
                    launcher.launch(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                    viewModel.changeIsNotificationPermissionDialogShown(false)
                }

                is NotificationSettingsUiEvent.NotificationPermissionDialogOnDismiss -> {
                    viewModel.changeIsNotificationPermissionDialogShown(false)
                }

                is NotificationSettingsUiEvent.Save -> {
                    viewModel.saveNotificationSettings()
                }

                is NotificationSettingsUiEvent.SaveSuccess -> {
                    showToast(context, "保存しました")
                    latestNavigateToSettingsScreen()
                }

                is NotificationSettingsUiEvent.SaveFailure -> {
                    showToast(context, "保存に失敗しました")
                }

                is NotificationSettingsUiEvent.NavigateToSettingsScreen -> {
                    latestNavigateToSettingsScreen()
                }
            }
        }.launchIn(this)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            WithmoTopAppBar(
                currentScreen = Screen.NotificationSettings,
                navigateBack = navigateToSettingsScreen,
            )
            NotificationSettingsScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
    if (uiState.isNotificationPermissionDialogShown) {
        NotificationPermissionDialog(
            onConfirm = {
                viewModel.onEvent(NotificationSettingsUiEvent.NotificationPermissionDialogOnConfirm)
            },
            onDismiss = {
                viewModel.onEvent(NotificationSettingsUiEvent.NotificationPermissionDialogOnDismiss)
            },
        )
    }
}
