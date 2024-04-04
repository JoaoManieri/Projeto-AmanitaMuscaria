package br.com.manieri.amanitamuscaria.ui.novaEntrada

import androidx.lifecycle.ViewModel
import br.com.manieri.amanitamuscaria.database.repository.CarroRepository
import br.com.manieri.amanitamuscaria.image.ImageManager
import br.com.manieri.amanitamuscaria.model.Avaria

class NovaEntradaViewModel(private val carroRepository: CarroRepository, private val imageManager: ImageManager) : ViewModel() {
//    fun create() {
//        viewModelScope.launch {
//            carroRepository.insert(Carro(1, "Saveiro"))
//            Log.w("DEBUG", "create: ${carroRepository.getAll()}", )
//        }
//    }

    fun getImages() : ArrayList<Avaria> = imageManager.getImages(arrayListOf())


}