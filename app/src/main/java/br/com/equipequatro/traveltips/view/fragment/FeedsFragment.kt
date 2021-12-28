package br.com.equipequatro.traveltips.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.equipequatro.traveltips.adapter.FeedPostAdapter
import br.com.equipequatro.traveltips.databinding.FragmentFeedsBinding
import br.com.equipequatro.traveltips.model.FeedPostItem
import br.com.equipequatro.traveltips.viewmodel.FeedsViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore


class FeedsFragment : Fragment() {

    private lateinit var postAdapter: FeedPostAdapter
    private lateinit var dashboardViewModel: FeedsViewModel
    private var _binding: FragmentFeedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dashboardViewModel =
            ViewModelProvider.NewInstanceFactory().create(FeedsViewModel::class.java)
        // ViewModelProvider(this).get(FeedsViewModel::class.java)

        postAdapter = FeedPostAdapter()
        binding.rvValoresFeed.adapter = postAdapter
        binding.rvValoresFeed.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        exibirDados()


        // val textView: TextView = binding.tvNomeCidadeFeed
        /* dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        }) */
        return root
    }

    private fun exibirDados() {

        val posts = mutableListOf<FeedPostItem>()
        var nome_usuario = ""
        var foto_usuario = ""
        var favoritado = false

        FirebaseFirestore.getInstance().collection("postagem")
            .get()
            .addOnSuccessListener(OnSuccessListener {
                for (documento in it.documents) {

                    val post = FeedPostItem(
                        id_post = documento.id,
                        nome_user = nome_usuario,
                        foto_user = foto_usuario,
                        favoritado = favoritado,
                        foto_url_post = documento.data?.get("fotoUrl").toString(),
                        codigo_user = documento.data?.get("usuarioCriadorUuid").toString(),
                        cidade_post = documento.data?.get("cidade").toString(),
                        estado_post = documento.data?.get("estado").toString(),
                        legenda_post = documento.data?.get("titulo").toString(),
                        descricao_post = documento.data?.get("descricao").toString(),
                        data_post = documento.data?.get("dataPostagem") as Timestamp
                    )

                    posts.add(post)
                }

                // Pegar os dados do usuário

                if (posts.size != 0) {

                    for (post in posts) {

                        FirebaseFirestore.getInstance().collection("usuario")
                            .whereEqualTo("uuid", post.codigo_user)
                            .get()
                            .addOnSuccessListener(OnSuccessListener {
                                for (usuario in it.documents) {
                                    post.nome_user = usuario.data?.get("nome").toString()
                                    post.foto_user = usuario.data?.get("fotoPerfilUrl").toString()
                                }

                                FirebaseFirestore.getInstance().collection("favorito")
                                    .whereEqualTo("uuidUsuario", post.codigo_user)
                                    .whereEqualTo("codigoPostagem", post.id_post)
                                    .get()
                                    .addOnSuccessListener(OnSuccessListener {
                                        if (it.documents.size != 0) {
                                            post.favoritado = true
                                            for (fav in it.documents) {
                                                post.id_favoritado = fav.id
                                            }
                                        } else {
                                            post.favoritado = false
                                            post.id_favoritado = ""
                                        }
                                        postAdapter.updateLista(posts)
                                    })
                                //postAdapter.updateLista(posts)
                            })
                    }
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}