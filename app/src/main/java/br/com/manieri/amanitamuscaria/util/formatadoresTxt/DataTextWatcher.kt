package br.com.manieri.amanitamuscaria.util.formatadoresTxt

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DataTextWatcher(private val input: EditText) : TextWatcher {

    private var isUpdating: Boolean = false
    private val mask = "##/##/####"

    override fun afterTextChanged(s: Editable?) {
        val str = unmask(s.toString())
        var mascara = ""
        if (isUpdating) {
            isUpdating = false
            return
        }
        var i = 0
        for (m in mask.toCharArray()) {
            if (m != '#' && str.length > i) {
                mascara += m
                continue
            }
            try {
                mascara += str[i]
            } catch (e: Exception) {
                break
            }
            i++
        }
        isUpdating = true
        input.setText(mascara)
        input.setSelection(mascara.length)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun unmask(s: String): String {
        return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
            .replace("[ ]".toRegex(), "").replace("[)]".toRegex(), "")
    }
}

