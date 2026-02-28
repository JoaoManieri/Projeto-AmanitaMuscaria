package br.com.manieri.amanitamuscaria.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSUserDefaults

private const val STATE_KEY = "app_state_json"

private class IosLocalStateStorage : LocalStateStorage {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun loadState(): String? = defaults.stringForKey(STATE_KEY)

    override fun saveState(value: String) {
        defaults.setObject(value, forKey = STATE_KEY)
    }
}

@Composable
actual fun rememberLocalStateStorage(): LocalStateStorage {
    return remember {
        IosLocalStateStorage()
    }
}
