package br.com.manieri.amanitamuscaria.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun StatusBadge(
    status: ServiceStatus,
    large: Boolean = false,
) {
    val tokens = LocalAutoCheckTokens.current
    val (label, bg, text, dot) = when (status) {
        ServiceStatus.IN_PROGRESS -> BadgeSpec(
            label = "Em andamento",
            bg = Color(0xFFDBEAFE),
            text = Color(0xFF1D4ED8),
            dot = tokens.statusBlue,
        )

        ServiceStatus.WAITING_PICKUP -> BadgeSpec(
            label = "Aguardando retirada",
            bg = Color(0xFFFFEDD5),
            text = Color(0xFFC2410C),
            dot = tokens.statusOrange,
        )

        ServiceStatus.COMPLETED -> BadgeSpec(
            label = "Finalizado",
            bg = Color(0xFFDCFCE7),
            text = Color(0xFF15803D),
            dot = tokens.statusGreen,
        )
    }
    Row(
        modifier = Modifier
            .background(bg, RoundedCornerShape(999.dp))
            .padding(
                horizontal = if (large) 16.dp else 10.dp,
                vertical = if (large) 8.dp else 4.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(if (large) 8.dp else 6.dp)
                .background(dot, CircleShape),
        )
        Text(
            text = label,
            style = if (large) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = text,
        )
    }
}

private data class BadgeSpec(
    val label: String,
    val bg: Color,
    val text: Color,
    val dot: Color,
)
