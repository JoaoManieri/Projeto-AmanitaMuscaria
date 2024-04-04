package br.com.manieri.amanitamuscaria.image

import br.com.manieri.amanitamuscaria.model.Avaria
import br.com.manieri.amanitamuscaria.model.Foto
import br.com.manieri.amanitamuscaria.model.factory.AvariaFactory
import java.io.File

class ImageManager {

    fun getImages(photoNames : ArrayList<Avaria>): ArrayList<Avaria> {

        val directory = File("/storage/emulated/0/Android/data/br.com.manieri.amanitamuscaria/files/Pictures/")
        val photos: Array<Avaria> = directory.listFiles { file -> file.isFile }?.map {
            AvariaFactory.create("Descrição tentanto descrição maior que o normal", it.absolutePath)// A descrição pode ser obtida de alguma forma, aqui é apenas um exemplo
        }!!.toTypedArray() ?: arrayOf()
//        val directory = File("/storage/emulated/0/Android/data/br.com.manieri.amanitamuscaria/files/Pictures/")
//        val photos: MutableList<File> = mutableListOf()
//
//        photoNames.forEach { photoName ->
//            val file = File(directory, photoName.uri)
//            if (file.exists()) {
//                photos.add(file)
//            }
//        }
        return ArrayList(photos.asList())
    }
}