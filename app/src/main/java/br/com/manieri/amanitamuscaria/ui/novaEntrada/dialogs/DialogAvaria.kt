package br.com.manieri.amanitamuscaria.ui.novaEntrada.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import br.com.manieri.amanitamuscaria.R
import br.com.manieri.amanitamuscaria.model.Avaria
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AlertDialog

class DialogAvaria(private val context: Context, private val avaria: Avaria) {


    private lateinit var imageView : ImageView

    var show: Boolean
        get() = builder.isShowing
        set(value) {
            if (value) {
                builder.show()
            } else {
                builder.dismiss()
            }
        }

    private val builder
        get() : AlertDialog {
        val imageView = createImageView()

        return MaterialAlertDialogBuilder(context).apply {
            setTitle(context.getString(R.string.detalhes_avaria))
            setView(imageView)
            setPositiveButton(context.getString(R.string.fechar), null)
            setBackground(
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.shape_dialog_translucido
                )
            )
            setOnDismissListener { cleanup() }
        }.create()
    }

    private fun createImageView(): ImageView {
        val bitmap = decodeBitmap()
        val rotatedBitmap = rotateBitmap(bitmap)

        imageView = ImageView(context)
        imageView.setImageBitmap(rotatedBitmap)

        return imageView
    }

    private fun decodeBitmap(): Bitmap {
        val bitmap = BitmapFactory.decodeFile(avaria.foto.getFile().path)
        val width = bitmap.width / 2
        val height = bitmap.height / 2
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(avaria.foto.rotacao)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun cleanup() {
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        bitmap.recycle()
    }
}
