package br.com.manieri.amanitamuscaria.image

import android.util.Log
import br.com.manieri.amanitamuscaria.model.Fotos
import java.io.File

class ImageManager {

    fun getImages(photoNames : ArrayList<Fotos>): Array<File> {

        val directory = File("/storage/emulated/0/Android/data/br.com.manieri.amanitamuscaria/files/Pictures/")
        val photos: Array<File> = directory.listFiles { file -> file.isFile } ?: arrayOf()
//        val directory = File("/storage/emulated/0/Android/data/br.com.manieri.amanitamuscaria/files/Pictures/")
//        val photos: MutableList<File> = mutableListOf()
//
//        photoNames.forEach { photoName ->
//            val file = File(directory, photoName.uri)
//            if (file.exists()) {
//                photos.add(file)
//            }
//        }
        return photos
    }
}