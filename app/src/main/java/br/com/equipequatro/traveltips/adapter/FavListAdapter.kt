package br.com.equipequatro.traveltips.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.model.FavItem
import br.com.equipequatro.traveltips.model.FeedPostItem
import com.bumptech.glide.Glide


class FavListAdapter: RecyclerView.Adapter<FavListAdapter.FavListHolder>() {

    private var listaFavs = emptyList<FavItem>()

    fun updateLista(lista: List<FavItem>) {
        listaFavs = lista
        notifyDataSetChanged()
    }

    class FavListHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nome_cidade_fav = view.findViewById<TextView>(R.id.tv_cidade_fav)
        var foto_cidade_fav = view.findViewById<ImageView>(R.id.iv_fav_foto_principal)
        var context = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavListHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_lista_favoritos, parent, false)

        return FavListAdapter.FavListHolder(view)
    }

    override fun onBindViewHolder(holder: FavListHolder, position: Int) {
        var post = listaFavs[position]
        holder.nome_cidade_fav.text = post.cidade_fav.toString()
        Glide.with(holder.context)
            .load(post.foto_fav)
            .into(holder.foto_cidade_fav)
    }

    override fun getItemCount() = listaFavs.size
}