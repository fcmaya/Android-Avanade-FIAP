package br.com.equipequatro.traveltips.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.model.FeedPostItem
import br.com.equipequatro.traveltips.model.NewImagePostItem
import java.time.LocalDate

class NewImagePostAdapter(var listaPosts: List<NewImagePostItem>): RecyclerView.Adapter<NewImagePostAdapter.NewImagePostHolder>() {

    class NewImagePostHolder(view: View): RecyclerView.ViewHolder(view) {
        var iv_foto_praia = view.findViewById<TextView>(R.id.tvwLocalizacao)
        var txtTituloPostagem = view.findViewById<TextView>(R.id.txtTituloPostagem)
        var tv_legenda_post = view.findViewById<TextView>(R.id.tv_legenda_foto_feed)
        var tv_descricao_post = view.findViewById<TextView>(R.id.tv_desc_foto_feed)
        var tv_num_comentarios = view.findViewById<TextView>(R.id.tv_numero_comentarios)
        var tv_data_post = view.findViewById<TextView>(R.id.tv_data_post_feed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewImagePostHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_lista_feed, parent, false)
        return NewImagePostHolder(view)
    }

    override fun onBindViewHolder(holder: NewImagePostHolder, position: Int) {
        var post = listaPosts[position]
//        holder.tv_nome_user.text = post.nome_user.toString()
//        holder.tv_cidade_user.text = post.cidade_user.toString()
        holder.tv_legenda_post.text = post.legenda_post.toString()
        holder.tv_descricao_post.text = post.descricao_post.toString()
        holder.tv_num_comentarios.text = "${post.num_comentarios} comentários"
        holder.tv_data_post.text = post.data_post.toString()
    }

    override fun getItemCount() = listaPosts.size
}