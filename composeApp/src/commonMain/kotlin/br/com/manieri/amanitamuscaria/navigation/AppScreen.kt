package br.com.manieri.amanitamuscaria.navigation

enum class AppScreen(
    val route: String,
    val label: String,
) {
    DASHBOARD("dashboard", "Atendimentos"),
    HISTORY("history", "Historico"),
    SETTINGS("settings", "Configuracoes"),
}
