package br.com.manieri.amanitamuscaria.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.manieri.amanitamuscaria.model.Client
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.model.Vehicle
import br.com.manieri.amanitamuscaria.model.WorkshopSettings

@Stable
class AutoCheckAppState(
    initialServices: List<Service>,
) {
    val services = mutableStateListOf<Service>().apply {
        addAll(initialServices)
    }

    var selectedServiceId: String? by mutableStateOf(initialServices.firstOrNull()?.id)
    var settings: WorkshopSettings by mutableStateOf(defaultSettings())

    fun addService(service: Service) {
        services.add(0, service)
        selectedServiceId = service.id
    }

    fun selectService(id: String?) {
        selectedServiceId = id
    }

    fun completeService(id: String) {
        val index = services.indexOfFirst { it.id == id }
        if (index >= 0) {
            val item = services[index]
            services[index] = item.copy(
                status = ServiceStatus.COMPLETED,
                exitDateLabel = "Hoje",
            )
        }
    }

    fun updateSettings(transform: (WorkshopSettings) -> WorkshopSettings) {
        settings = transform(settings)
    }
}

@Composable
fun rememberAutoCheckAppState(): AutoCheckAppState {
    return remember {
        AutoCheckAppState(initialServices = sampleServices())
    }
}

private fun sampleServices(): List<Service> = listOf(
    Service(
        id = "1",
        plate = "ABC-1234",
        vehicle = Vehicle(
            plate = "ABC-1234",
            brand = "Toyota",
            model = "Corolla",
            year = 2020,
            color = "Prata",
            mileage = 45000,
        ),
        client = Client(
            name = "Joao Silva",
            phone = "(11) 98765-4321",
            email = "joao.silva@email.com",
        ),
        status = ServiceStatus.IN_PROGRESS,
        entryDateLabel = "15/01/2024 as 09:00",
        observations = "Troca de oleo e revisao dos 40.000km. Verificar freios dianteiros.",
    ),
    Service(
        id = "2",
        plate = "DEF-5678",
        vehicle = Vehicle(
            plate = "DEF-5678",
            brand = "Honda",
            model = "Civic",
            year = 2022,
            color = "Preto",
            mileage = 15000,
        ),
        client = Client(
            name = "Maria Santos",
            phone = "(11) 91234-5678",
            email = "maria.santos@email.com",
        ),
        status = ServiceStatus.WAITING_PICKUP,
        entryDateLabel = "14/01/2024 as 14:30",
        observations = "Alinhamento e balanceamento. Verificar suspensao.",
    ),
    Service(
        id = "3",
        plate = "GHI-9012",
        vehicle = Vehicle(
            plate = "GHI-9012",
            brand = "Volkswagen",
            model = "Golf",
            year = 2019,
            color = "Branco",
            mileage = 62000,
        ),
        client = Client(
            name = "Pedro Oliveira",
            phone = "(11) 94567-8901",
        ),
        status = ServiceStatus.COMPLETED,
        entryDateLabel = "10/01/2024 as 10:00",
        exitDateLabel = "12/01/2024 as 16:00",
        observations = "Reparo no sistema de ar condicionado.",
    ),
)

private fun defaultSettings(): WorkshopSettings = WorkshopSettings(
    workshopName = "Oficina AutoCheck Pro",
    address = "Rua Exemplo, 123 - Sao Paulo/SP",
    phone = "(11) 3333-4444",
    cnpj = "12.345.678/0001-90",
    reportHeader = "Relatorio de Servico",
    showLogoInReport = true,
    requireSignature = true,
)
