package br.com.manieri.amanitamuscaria.ui.novaEntrada.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.amanitamuscaria.R
import br.com.manieri.amanitamuscaria.ui.novaEntrada.dialogs.showDialogFotos
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class PhotoAdapter(private val photos: ArrayList<File>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_avaria)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_preview_avarias, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photoFile = photos[position]
        Glide.with(holder.itemView.context)
            .load(photoFile)
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            showDialogFotos(holder.itemView.context, photoFile)
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    private fun getNextPos() = if ((photos.size - 1) <= 0) {
        0
    } else {
        photos.size - 1
    }

    fun updatePhotos(newPhoto: File) {
        photos.add(newPhoto)
        notifyItemInserted(getNextPos())
    }

}

