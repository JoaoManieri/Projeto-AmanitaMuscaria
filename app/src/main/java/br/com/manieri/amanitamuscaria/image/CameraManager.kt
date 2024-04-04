package br.com.manieri.amanitamuscaria.image

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import android.app.Activity.RESULT_OK
import androidx.activity.result.ActivityResultLauncher
import br.com.manieri.amanitamuscaria.ui.novaEntrada.adapter.PhotoAdapter


class CameraManager(private val fragment: Fragment) {

    private val REQUEST_TAKE_PHOTO = 1
    private var currentPhotoPath: String = ""

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(fragment.requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        fragment.requireContext(),
                        "br.com.manieri.amanitamuscaria.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = fragment.requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, adapter: PhotoAdapter) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            adapter.updatePhotos(File(currentPhotoPath))
        }
    }
}
