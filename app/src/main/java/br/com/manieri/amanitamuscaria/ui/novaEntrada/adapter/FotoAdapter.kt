package br.com.manieri.amanitamuscaria.ui.novaEntrada.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.manieri.amanitamuscaria.R
import br.com.manieri.amanitamuscaria.model.Avaria
import br.com.manieri.amanitamuscaria.ui.novaEntrada.dialogs.DialogAvaria
import com.bumptech.glide.Glide

class FotoAdapter(private val avarias: ArrayList<Avaria>) :
    RecyclerView.Adapter<FotoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_avaria)
        val tvDescricao: TextView = itemView.findViewById(R.id.tv_descricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_preview_avarias, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foto = avarias[position]
        Glide.with(holder.itemView.context)
            .load(foto.foto.getFile())
            .centerCrop()
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            DialogAvaria(holder.itemView.context, foto).show = true
        }

        holder.tvDescricao.text = foto.descricao
    }

    override fun getItemCount(): Int {
        return avarias.size
    }

    private fun getNextPos() = if ((avarias.size - 1) <= 0) {
        0
    } else {
        avarias.size - 1
    }

    fun updatePhotos(novaAvaria: Avaria) {
        avarias.add(novaAvaria)
        notifyItemInserted(getNextPos())
    }

}

