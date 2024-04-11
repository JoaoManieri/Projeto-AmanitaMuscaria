package br.com.manieri.amanitamuscaria.util.formatadoresTxt

import android.text.Editable
import android.text.TextWatcher

class PlacaVeiculoTextWatcher : TextWatcher {
    private var isUpdating: Boolean = false

    override fun afterTextChanged(editable: Editable?) {
        if (isUpdating) {
            return
        }

        val cleanString = editable.toString().filter { it.isLetterOrDigit() }

        isUpdating = true

        when (cleanString.length) {
            in 0..3 -> {
                editable?.replace(0, editable.length, cleanString.uppercase())
            }
            4 -> {
                editable?.replace(0, editable.length, cleanString.substring(0, 3).uppercase() + "-" + cleanString[3])
            }
            in 5..7 -> {
                editable?.replace(0, editable.length, cleanString.substring(0, 3).uppercase() + "-" + cleanString.substring(3).uppercase())
            }
            else -> {
                editable?.delete(cleanString.length - 1, cleanString.length)
            }
        }

        isUpdating = false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Não é necessário para este caso
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Não é necessário para este caso
    }
}
