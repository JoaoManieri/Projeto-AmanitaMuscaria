package br.com.manieri.amanitamuscaria.state

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private const val PREFS_NAME = "autocheck_local_state"
private const val STATE_KEY = "app_state_json"

private class AndroidLocalStateStorage(
    context: Context,
) : LocalStateStorage {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun loadState(): String? = prefs.getString(STATE_KEY, null)

    override fun saveState(value: String) {
        prefs.edit().putString(STATE_KEY, value).apply()
    }
}

@Composable
actual fun rememberLocalStateStorage(): LocalStateStorage {
    val context = LocalContext.current
    return remember(context) {
        AndroidLocalStateStorage(context.applicationContext)
    }
}
