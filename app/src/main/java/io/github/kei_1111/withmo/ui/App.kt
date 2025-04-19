package io.github.kei_1111.withmo.ui

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.unity3d.player.UnityPlayer
import io.github.kei_1111.withmo.domain.model.Screen
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsScreen
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsScreen
import io.github.kei_1111.withmo.ui.screens.favorite_app_settings.FavoriteAppSettingsScreen
import io.github.kei_1111.withmo.ui.screens.home.HomeScreen
import io.github.kei_1111.withmo.ui.screens.notification_settings.NotificationSettingsScreen
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingScreen
import io.github.kei_1111.withmo.ui.screens.settings.SettingsScreen
import io.github.kei_1111.withmo.ui.screens.side_button_settings.SideButtonSettingsScreen
import io.github.kei_1111.withmo.ui.screens.sort_settings.SortSettingsScreen
import io.github.kei_1111.withmo.ui.screens.theme_settings.ThemeSettingsScreen

@RequiresApi(Build.VERSION_CODES.R)
@Suppress("ModifierMissing", "LongMethod", "CyclomaticComplexMethod")
@Composable
fun App(
    startScreen: Screen,
) {
    var currentScreen by remember { mutableStateOf(startScreen) }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        UnityView(
            modifier = Modifier.fillMaxSize(),
        )

        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                when {
                    initialState !in listOf(
                        Screen.Home,
                        Screen.Settings,
                    ) && targetState is Screen.Settings -> {
                        slideInHorizontally { -it }.togetherWith(slideOutHorizontally { it })
                    }

                    targetState is Screen.Home -> {
                        fadeIn().togetherWith(slideOutVertically { it })
                    }

                    targetState is Screen.Settings -> {
                        slideInVertically { it }.togetherWith(ExitTransition.None)
                    }

                    targetState is Screen.Onboarding -> {
                        fadeIn().togetherWith(ExitTransition.None)
                    }

                    else -> {
                        slideInHorizontally { it }.togetherWith(ExitTransition.KeepUntilTransitionsFinished)
                    }
                }
            },
        ) { targetState ->
            val navigateToSettingScreen = { currentScreen = Screen.Settings }

            when (targetState) {
                is Screen.Onboarding -> {
                    OnboardingScreen(
                        navigateToHomeScreen = { currentScreen = Screen.Home },
                    )
                }

                is Screen.Home -> {
                    HomeScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.Settings -> {
                    SettingsScreen(
                        onNavigateClockSettingsButtonClick = {
                            currentScreen = Screen.ClockSettings
                        },
                        onNavigateAppIconSettingsButtonClick = {
                            currentScreen = Screen.AppIconSettings
                        },
                        onNavigateFavoriteAppSettingsButtonClick = {
                            currentScreen = Screen.FavoriteAppSettings
                        },
                        onNavigateSideButtonSettingsButtonClick = {
                            currentScreen = Screen.SideButtonSettings
                        },
                        onNavigateSortSettingsButtonClick = { currentScreen = Screen.SortSettings },
                        onNavigateNotificationSettingsButtonClick = {
                            currentScreen = Screen.NotificationSettings
                        },
                        onNavigateThemeSettingsButtonClick = {
                            currentScreen = Screen.ThemeSettings
                        },
                        onBackButtonClick = { currentScreen = Screen.Home },
                    )
                }

                is Screen.NotificationSettings -> {
                    NotificationSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.ClockSettings -> {
                    ClockSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.AppIconSettings -> {
                    AppIconSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.FavoriteAppSettings -> {
                    FavoriteAppSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.SideButtonSettings -> {
                    SideButtonSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.SortSettings -> {
                    SortSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }

                is Screen.ThemeSettings -> {
                    ThemeSettingsScreen(
                        navigateToSettingsScreen = navigateToSettingScreen,
                    )
                }
            }
        }
    }
}

@Composable
fun UnityView(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val unityView = remember { UnityManager.player.view }

    val container = remember { FrameLayout(context) }

    AndroidView(
        factory = { container },
        update = { host ->
            if (unityView.parent != host) {
                (unityView.parent as? ViewGroup)?.removeView(unityView)
                host.addView(unityView,
                    ViewGroup.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT
                    )
                )
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

object UnityManager {
    private var _player: UnityPlayer? = null
    val player: UnityPlayer
        get() = _player ?: throw IllegalStateException("UnityPlayer not initialized")

    fun init(context: Context) {
        if (_player != null) return
        _player = UnityPlayer(context).apply {
            init(settings.getInt("gles_mode", 1), false)
        }
    }

    fun resume()  = _player?.resume()
    fun pause()   = _player?.pause()
    fun quit()    = _player?.quit()
    fun focusGained(gained: Boolean) = _player?.windowFocusChanged(gained)
}
