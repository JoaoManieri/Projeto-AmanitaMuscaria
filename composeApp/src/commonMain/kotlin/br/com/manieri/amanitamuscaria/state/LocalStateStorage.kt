package br.com.manieri.amanitamuscaria.state

import androidx.compose.runtime.Composable

interface LocalStateStorage {
    fun loadState(): String?
    fun saveState(value: String)
}

@Composable
expect fun rememberLocalStateStorage(): LocalStateStorage
