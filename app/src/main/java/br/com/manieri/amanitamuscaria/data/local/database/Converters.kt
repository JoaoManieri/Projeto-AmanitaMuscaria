package br.com.manieri.amanitamuscaria.data.local.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(value: List<String>?): String? = value?.joinToString(separator = "|")

    @TypeConverter
    fun toList(value: String?): List<String> = value?.split("|")?.filter { it.isNotBlank() } ?: emptyList()
}
