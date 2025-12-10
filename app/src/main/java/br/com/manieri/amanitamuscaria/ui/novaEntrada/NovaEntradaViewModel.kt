package br.com.manieri.amanitamuscaria.ui.novaEntrada

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.manieri.amanitamuscaria.database.repository.CarroRepository
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.UnauthorizedException
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

class NovaEntradaViewModel(
    private val carroRepository: CarroRepository,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Esta tela agora utiliza tratamento centralizado de erros."
    }
    val text: LiveData<String> = _text

    fun saveData(simulateFailure: Boolean = false) {
        viewModelScope.launch {
            try {
                if (simulateFailure) {
                    throw TimeoutException("Simulação de lentidão ao salvar.")
                }
                carroRepository.getAll()
                _text.postValue("Dados processados com sucesso.")
            } catch (e: Exception) {
                errorHandler.handle(e)
            }
        }
    }

    fun simulateUnauthorized() {
        errorHandler.handle(UnauthorizedException("Sessão expirada."))
    }
}
