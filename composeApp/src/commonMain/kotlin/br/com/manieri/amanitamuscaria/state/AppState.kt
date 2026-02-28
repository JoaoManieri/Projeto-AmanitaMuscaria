package br.com.manieri.amanitamuscaria.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.model.WorkshopSettings
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val STATE_SCHEMA_VERSION = 1

@Serializable
private data class PersistedAppState(
    val version: Int = STATE_SCHEMA_VERSION,
    val services: List<Service> = emptyList(),
    val selectedServiceId: String? = null,
    val settings: WorkshopSettings = defaultSettings(),
)

@Stable
class AutoCheckAppState(
    private val storage: LocalStateStorage,
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val services = mutableStateListOf<Service>()
    var selectedServiceId: String? by mutableStateOf(null)
    var settings: WorkshopSettings by mutableStateOf(defaultSettings())

    init {
        val restoredState = loadPersistedState()
        services.addAll(restoredState?.services ?: emptyList())
        selectedServiceId = sanitizeSelectedServiceId(restoredState?.selectedServiceId)
        settings = restoredState?.settings ?: defaultSettings()
    }

    fun addService(service: Service) {
        services.add(0, service)
        selectedServiceId = service.id
        persistState()
    }

    fun selectService(id: String?) {
        selectedServiceId = sanitizeSelectedServiceId(id)
        persistState()
    }

    fun completeService(id: String) {
        val index = services.indexOfFirst { it.id == id }
        if (index >= 0) {
            val item = services[index]
            services[index] = item.copy(
                status = ServiceStatus.COMPLETED,
                exitDateLabel = "Hoje",
            )
            persistState()
        }
    }

    fun updateSettings(transform: (WorkshopSettings) -> WorkshopSettings) {
        settings = transform(settings)
        persistState()
    }

    private fun sanitizeSelectedServiceId(selectedId: String?): String? {
        if (selectedId == null) return services.firstOrNull()?.id
        return selectedId.takeIf { id -> services.any { it.id == id } } ?: services.firstOrNull()?.id
    }

    private fun loadPersistedState(): PersistedAppState? {
        val raw = storage.loadState() ?: return null
        return runCatching {
            json.decodeFromString<PersistedAppState>(raw)
        }.getOrNull()
    }

    private fun persistState() {
        val snapshot = PersistedAppState(
            services = services.toList(),
            selectedServiceId = selectedServiceId,
            settings = settings,
        )
        val encoded = runCatching {
            json.encodeToString(snapshot)
        }.getOrNull() ?: return
        storage.saveState(encoded)
    }
}

@Composable
fun rememberAutoCheckAppState(): AutoCheckAppState {
    val storage = rememberLocalStateStorage()
    return remember(storage) {
        AutoCheckAppState(storage = storage)
    }
}

private fun defaultSettings(): WorkshopSettings = WorkshopSettings(
    workshopName = "Oficina AutoCheck Pro",
    address = "Rua Exemplo, 123 - Sao Paulo/SP",
    phone = "(11) 3333-4444",
    cnpj = "12.345.678/0001-90",
    reportHeader = "Relatorio de Servico",
    showLogoInReport = true,
    requireSignature = true,
)
