package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository
import java.text.Normalizer
import kotlin.math.max

class SearchVehicleEntriesUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(query: String): List<VehicleEntry> {
        val all = repository.getAll()
        if (query.isBlank()) return all.sortedByDescending { it.createdAt }
        val normalizedQuery = normalize(query)
        return all
            .map { entry ->
                val score = scoreEntry(entry, normalizedQuery)
                entry to score
            }
            .sortedWith(compareByDescending<Pair<VehicleEntry, Int>> { it.second }
                .thenByDescending { it.first.createdAt })
            .map { it.first }
    }

    private fun scoreEntry(entry: VehicleEntry, query: String): Int {
        val fields = listOf(
            entry.plate,
            entry.brand,
            entry.model,
            entry.customerName
        ).map(::normalize)

        var score = 0
        fields.forEach {
            if (it == query) score += 3
            else if (it.startsWith(query)) score += 2
            else if (it.contains(query)) score += 1
        }
        return score
    }

    private fun normalize(value: String): String {
        val clean = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return clean.lowercase()
    }
}
