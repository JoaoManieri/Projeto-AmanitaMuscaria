package br.com.manieri.amanitamuscaria.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.prefs.Preferences

private const val PREFS_NODE = "br.com.manieri.amanitamuscaria"
private const val STATE_KEY = "app_state_json"

private class DesktopLocalStateStorage : LocalStateStorage {
    private val prefs = Preferences.userRoot().node(PREFS_NODE)

    override fun loadState(): String? = prefs.get(STATE_KEY, null)

    override fun saveState(value: String) {
        prefs.put(STATE_KEY, value)
        prefs.flush()
    }
}

@Composable
actual fun rememberLocalStateStorage(): LocalStateStorage {
    return remember {
        DesktopLocalStateStorage()
    }
}
