package br.com.manieri.amanitamuscaria.ui.novaEntrada.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import br.com.manieri.amanitamuscaria.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

fun showDialogFotos(context: Context, photoFile: File) {
    val bitmap = BitmapFactory.decodeFile(photoFile.path)
    val width = bitmap.width / 2
    val height = bitmap.height / 2
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)

    val matrix = Matrix()
    matrix.postRotate(90f)
    val rotatedBitmap = Bitmap.createBitmap(resizedBitmap, 0, 0, width, height, matrix, true)

    val imageView = ImageView(context).apply {
        setImageBitmap(rotatedBitmap)
    }

    val materialAlertDialog = MaterialAlertDialogBuilder(context)
        .setTitle(context.getString(R.string.detalhes_avaria))
        .setView(imageView)
        .setPositiveButton(context.getString(R.string.fechar), null)
        .setBackground(
            AppCompatResources.getDrawable(
                context,
                R.drawable.shape_dialog_translucido
            )
        ).create()

    materialAlertDialog.setOnDismissListener {
        bitmap.recycle()
        resizedBitmap.recycle()
    }
    materialAlertDialog.show()
}