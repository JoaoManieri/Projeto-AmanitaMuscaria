package br.com.manieri.amanitamuscaria.model.factory

import br.com.manieri.amanitamuscaria.model.Avaria
import br.com.manieri.amanitamuscaria.model.Foto


/**
 * Factory para criar instâncias de Foto
 */
object FotoFactory {
    fun create(fotoUri: String, rotacao: Float = 90f): Foto {
        return Foto(fotoUri, rotacao)
    }
}

/**
 * Factory para criar instâncias de Avaria
 */
object AvariaFactory {
    fun create(descricao: String, fotoUri: String, rotacao: Float = 90f): Avaria {
        val foto = FotoFactory.create(fotoUri, rotacao)
        return Avaria(descricao, foto)
    }
}