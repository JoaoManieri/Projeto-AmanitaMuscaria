package br.com.manieri.amanitamuscaria.util.formatadoresTxt

import com.google.android.material.slider.LabelFormatter

class PercentLabelFormatter : LabelFormatter {
    override fun getFormattedValue(value: Float): String {
        return when (value.toInt()) {
            0 -> "Reserva"
            25 -> "1/4"
            50 -> "1/2"
            75 -> "3/4"
            100 -> "Cheio"
            else -> "${value.toInt()}%"
        }
    }
}