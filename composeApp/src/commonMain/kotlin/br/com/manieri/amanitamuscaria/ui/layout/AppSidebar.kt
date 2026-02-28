package br.com.manieri.amanitamuscaria.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ViewKanban
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import amanitamuscaria.composeapp.generated.resources.Res
import amanitamuscaria.composeapp.generated.resources.logo
import br.com.manieri.amanitamuscaria.navigation.AppScreen
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun AppSidebar(
    activeScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(tokens.sidebarWidth)
            .background(tokens.sidebarBackground)
            .padding(vertical = 24.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(tokens.radiusLg))
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(38.dp),
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SidebarItem(
                label = "Atendimentos",
                isActive = activeScreen == AppScreen.DASHBOARD,
                tint = tokens.sidebarInactive,
                activeTint = Color.White,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.ViewKanban,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = { onScreenSelected(AppScreen.DASHBOARD) },
            )
            SidebarItem(
                label = "Historico",
                isActive = activeScreen == AppScreen.HISTORY,
                tint = tokens.sidebarInactive,
                activeTint = Color.White,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.History,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = { onScreenSelected(AppScreen.HISTORY) },
            )
            SidebarItem(
                label = "Configuracoes",
                isActive = activeScreen == AppScreen.SETTINGS,
                tint = tokens.sidebarInactive,
                activeTint = Color.White,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                onClick = { onScreenSelected(AppScreen.SETTINGS) },
            )
        }

        Box(modifier = Modifier.weight(1f))

        Text(
            text = "v1.0",
            style = MaterialTheme.typography.labelSmall,
            color = tokens.sidebarInactive,
        )
    }
}

@Composable
private fun SidebarItem(
    label: String,
    isActive: Boolean,
    tint: Color,
    activeTint: Color,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    val indicatorHeight = animateDpAsState(
        targetValue = if (isActive) 32.dp else 0.dp,
        animationSpec = tween(durationMillis = 220),
        label = "sidebar-indicator",
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(tokens.radiusLg))
            .background(if (isActive) tokens.sidebarActiveBackground else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(visible = isActive) {
            Box(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .size(width = 4.dp, height = indicatorHeight.value)
                    .clip(RoundedCornerShape(topEnd = 999.dp, bottomEnd = 999.dp))
                    .background(tokens.sidebarAccent),
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier.background(Color.Transparent),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.material3.LocalContentColor provides if (isActive) activeTint else tint,
                ) {
                    icon()
                }
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isActive) activeTint else tint,
                textAlign = TextAlign.Center,
            )
        }
    }
}
