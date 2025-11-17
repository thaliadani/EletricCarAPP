package com.example.eletriccarapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eletriccarapp.R
import com.example.eletriccarapp.domain.Carro
import com.squareup.picasso.Picasso

class CarAdapter(private val carros: List<Carro>, private val isFavoriteScreen: Boolean = false) :
    RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    var carItemLister: (Carro) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carro_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carros[position], isFavoriteScreen)
        holder.carItemLister = carItemLister
    }

    override fun getItemCount(): Int = carros.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val preco: TextView = view.findViewById(R.id.tv_preco_valor)
        private val bateria: TextView = view.findViewById(R.id.tv_bateria_valor)
        private val potencia: TextView = view.findViewById(R.id.tv_potencia_valor)
        private val recarga: TextView = view.findViewById(R.id.tv_recarga_valor)
        private val photo: ImageView = view.findViewById(R.id.iv_carro)
        private val favorite: ImageButton = view.findViewById(R.id.btn_favoritar)
        var carItemLister: (Carro) -> Unit = {}
        fun bind(carro: Carro, isFavoriteScreen: Boolean) {
            preco.text = carro.preco
            bateria.text = carro.bateria
            potencia.text = carro.potencia
            recarga.text = carro.recarga

            if (carro.isFavorite) {
                favorite.setImageResource(R.drawable.ic_start_selected)
            } else {
                favorite.setImageResource(R.drawable.ic_star)
            }

            favorite.setOnClickListener {
                carro.isFavorite = !carro.isFavorite
                carItemLister(carro)
                setupFavoriteImage(carro)
            }
            Picasso.get().load(carro.urlPhoto).into(photo)
        }

        private fun setupFavoriteImage(carro: Carro) {
            if (carro.isFavorite) {
                favorite.setImageResource(R.drawable.ic_start_selected)
            } else {
                favorite.setImageResource(R.drawable.ic_star)
            }
        }
    }
}
