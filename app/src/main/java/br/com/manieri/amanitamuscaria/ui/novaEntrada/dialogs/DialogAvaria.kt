package br.com.manieri.amanitamuscaria.ui.novaEntrada.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import br.com.manieri.amanitamuscaria.R
import br.com.manieri.amanitamuscaria.databinding.DialogCheckAvariaBinding
import br.com.manieri.amanitamuscaria.model.Avaria
import br.com.manieri.amanitamuscaria.util.editoresImg.ImageHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DialogAvaria(private val context: Context, private val avaria: Avaria) : KoinComponent {


    private val binding: DialogCheckAvariaBinding by lazy {
        DialogCheckAvariaBinding.inflate(LayoutInflater.from(context))
    }

    private val builder: AlertDialog by lazy {

        binding.ivFotoAvaria.setImageURI(Uri.parse(avaria.foto.fotoUri))
        binding.tvDescricao.text = avaria.descricao

        binding.textButtonFechar.setOnClickListener {
            dismissAlertDialog()
        }

        MaterialAlertDialogBuilder(context).apply {
            setView(binding.root)
            setOnDismissListener { cleanup() }
        }.create()
    }

    private fun dismissAlertDialog() {
        builder.dismiss()
    }

    var show: Boolean
        get() = builder.isShowing
        set(value) {
            if (value) builder.show() else builder.dismiss()
        }

    private fun cleanup() {
        //imageHandler.cleanupBitmap()
    }
}
