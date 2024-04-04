package br.com.manieri.amanitamuscaria.model

import java.io.File

data class Foto(
    val fotoUri : String,
    val rotacao : Float = 90f
){

    private val file : File = File(fotoUri)

    fun getFile() : File {
        return file
    }
}