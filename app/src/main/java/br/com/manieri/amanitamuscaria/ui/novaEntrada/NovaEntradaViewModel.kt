package br.com.manieri.amanitamuscaria.ui.novaEntrada

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.manieri.amanitamuscaria.database.repository.CarroRepository
import br.com.manieri.amanitamuscaria.model.Carro
import kotlinx.coroutines.launch

class NovaEntradaViewModel(private val carroRepository: CarroRepository) : ViewModel() {
    fun create() {
        viewModelScope.launch {
            carroRepository.insert(Carro(1, "Saveiro"))
            Log.w("DEBUG", "create: ${carroRepository.getAll()}", )
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}