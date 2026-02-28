package br.com.manieri.amanitamuscaria

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import br.com.manieri.amanitamuscaria.navigation.AppScreen
import br.com.manieri.amanitamuscaria.state.rememberAutoCheckAppState
import br.com.manieri.amanitamuscaria.ui.layout.AppSidebar
import br.com.manieri.amanitamuscaria.ui.screens.dashboard.DashboardScreen
import br.com.manieri.amanitamuscaria.ui.screens.history.HistoryScreen
import br.com.manieri.amanitamuscaria.ui.screens.settings.SettingsScreen
import br.com.manieri.amanitamuscaria.ui.theme.AutoCheckTheme
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
@Preview
fun App() {
    AutoCheckTheme {
        var activeScreen by rememberSaveable { mutableStateOf(AppScreen.DASHBOARD) }
        val appState = rememberAutoCheckAppState()
        val tokens = LocalAutoCheckTokens.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(tokens.canvasBackground),
            contentAlignment = Alignment.TopStart,
        ) {
            AppSidebar(
                activeScreen = activeScreen,
                onScreenSelected = { activeScreen = it },
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = tokens.sidebarWidth)
                    .background(tokens.canvasBackground),
            ) {
                AnimatedContent(
                    targetState = activeScreen,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) +
                            slideInHorizontally(animationSpec = tween(300)) { fullWidth -> fullWidth / 8 })
                            .togetherWith(
                                fadeOut(animationSpec = tween(300)) +
                                    slideOutHorizontally(animationSpec = tween(300)) { fullWidth -> -fullWidth / 8 }
                            )
                    },
                    label = "page-transition",
                ) { screen ->
                    when (screen) {
                        AppScreen.DASHBOARD -> DashboardScreen(
                            services = appState.services,
                            selectedServiceId = appState.selectedServiceId,
                            onSelectService = appState::selectService,
                            onAddService = appState::addService,
                            onCompleteService = appState::completeService,
                        )

                        AppScreen.HISTORY -> HistoryScreen(services = appState.services)
                        AppScreen.SETTINGS -> SettingsScreen(
                            settings = appState.settings,
                            onUpdateSettings = { updated -> appState.updateSettings { updated } },
                        )
                    }
                }
            }
        }
    }
}
