package br.com.manieri.amanitamuscaria.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.manieri.amanitamuscaria.navigation.AppScreen
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun PlaceholderScreen(screen: AppScreen) {
    val tokens = LocalAutoCheckTokens.current
    val description = when (screen) {
        AppScreen.DASHBOARD -> "Etapa A concluida: shell e navegacao base da Dashboard."
        AppScreen.HISTORY -> "Etapa A concluida: shell e navegacao base do Historico."
        AppScreen.SETTINGS -> "Etapa A concluida: shell e navegacao base de Configuracoes."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tokens.canvasBackground),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .background(tokens.cardBackground, RoundedCornerShape(tokens.radiusLg))
                .padding(horizontal = 32.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = screen.label,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = tokens.textPrimary,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                color = tokens.textSecondary,
            )
        }
    }
}
