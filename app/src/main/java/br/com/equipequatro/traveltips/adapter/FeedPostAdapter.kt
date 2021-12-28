package br.com.equipequatro.traveltips.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.model.FavItem
import br.com.equipequatro.traveltips.model.FeedPostItem
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.newFixedThreadPoolContext
import java.security.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class FeedPostAdapter() : RecyclerView.Adapter<FeedPostAdapter.FeedPostHolder>() {

    private var listaPosts = emptyList<FeedPostItem>()

    fun updateLista(lista: List<FeedPostItem>) {
        listaPosts = lista
        notifyDataSetChanged()
    }


    class FeedPostHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_nome_user = view.findViewById<TextView>(R.id.tv_nome_user_post)
        var tv_cidade_post = view.findViewById<TextView>(R.id.tv_nome_cidade_feed3)
        var tv_estado_post = view.findViewById<TextView>(R.id.tv_nome_estado_feed2)
        var tv_legenda_post = view.findViewById<TextView>(R.id.tv_legenda_foto_feed)
        var tv_descricao_post = view.findViewById<TextView>(R.id.tv_desc_foto_feed)
        var tv_data_post = view.findViewById<TextView>(R.id.tv_data_post_feed)
        var iv_foto_post = view.findViewById<ImageView>(R.id.iv_foto_praia)
        var iv_foto_perfil_user = view.findViewById<ImageView>(R.id.iv_foto_user)
        var iv_favorito = view.findViewById<ImageView>(R.id.iv_favorito)
        var context = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedPostHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_lista_feed, parent, false)

        return FeedPostHolder(view)
    }

    override fun onBindViewHolder(holder: FeedPostHolder, position: Int) {
        var post = listaPosts[position]
        holder.tv_nome_user.text = post.nome_user.toString()
        holder.tv_cidade_post.text = post.cidade_post.toString()
        holder.tv_estado_post.text = post.estado_post.toString()
        holder.tv_legenda_post.text = post.legenda_post.toString()
        holder.tv_descricao_post.text = post.descricao_post.toString()


        if (post.favoritado) {
            holder.iv_favorito.setColorFilter(Color.argb(255, 255, 0, 0), PorterDuff.Mode.SRC_IN)
        } else {
            holder.iv_favorito.setColorFilter(
                Color.argb(255, 170, 170, 170),
                PorterDuff.Mode.MULTIPLY
            )
        }

        holder.iv_favorito.setOnClickListener {
            if (post.favoritado) {
                val documentReference = FirebaseFirestore.getInstance().collection("favorito")

                documentReference
                    .whereEqualTo("uuidUsuario", post.codigo_user)
                    .whereEqualTo("codigoPostagem", post.id_post)
                    .get()
                    .addOnSuccessListener(OnSuccessListener {
                        if (it.documents.size != 0) {
                            post.favoritado = true
                            for (fav in it.documents) {
                                documentReference.document(fav.id).delete()
                            }
                        }
                    })

                post.favoritado = false
                holder.iv_favorito.setColorFilter(
                    Color.argb(255, 170, 170, 170),
                    PorterDuff.Mode.MULTIPLY
                )
            } else {
                var fav_post = FavItem(
                    post.id_post, post.codigo_user, com.google.firebase.Timestamp.now()
                )
                val documentReference = FirebaseFirestore.getInstance().collection("favorito")
                documentReference.document().set(fav_post)
                post.favoritado = true
                holder.iv_favorito.setColorFilter(
                    Color.argb(255, 255, 0, 0),
                    PorterDuff.Mode.MULTIPLY
                )
            }
        }


        var data = post.data_post
        var data_timestamp = data.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var data_post = data_timestamp
        var data_final = data_post.format(formato)
        holder.tv_data_post.text = data_final
        Glide.with(holder.context)
            .load(post.foto_user)
            .into(holder.iv_foto_perfil_user)
        Glide.with(holder.context)
            .load(post.foto_url_post)
            .into(holder.iv_foto_post)

    }

    override fun getItemCount() = listaPosts.size
}