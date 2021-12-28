package br.com.equipequatro.traveltips.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.equipequatro.traveltips.adapter.FavListAdapter
import br.com.equipequatro.traveltips.adapter.FeedPostAdapter
import br.com.equipequatro.traveltips.databinding.FragmentHomeBinding
import br.com.equipequatro.traveltips.model.FavItem
import br.com.equipequatro.traveltips.model.FeedPostItem
import br.com.equipequatro.traveltips.repository.SharedPreferencesRepository
import br.com.equipequatro.traveltips.util.getGreetingMessage
import br.com.equipequatro.traveltips.viewmodel.HomeViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var favListAdapter: FavListAdapter
    private var _binding: FragmentHomeBinding? = null

    private lateinit var auth: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.txtNome.text = SharedPreferencesRepository.getPreferences(context, "displayName")
        binding.txtSaudacao.text = getGreetingMessage(context = this.requireContext())

        favListAdapter = FavListAdapter()
        binding.rvBarraFavoritos.adapter = favListAdapter
        binding.rvBarraFavoritos.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        exibirFavoritos()

        return root


    }

    private fun exibirFavoritos() {
        val favoritos = mutableListOf<FavItem>()

        FirebaseFirestore.getInstance().collection("favorito")
            .get()
            .addOnSuccessListener(OnSuccessListener {
                for (documento in it.documents) {

                    val fav = FavItem(
                        id_favorito = documento.id,
                        codigoPostagem = documento.data?.get("codigoPostagem").toString(),
                        uuidUsuario = documento.data?.get("uuidUsuario").toString(),
                        foto_fav = "",
                        cidade_fav = ""
                    )

                    favoritos.add(fav)
                }

                // Pegar os dados da postagem
                if (favoritos.size != 0) {

                    for (fav in favoritos) {

                        FirebaseFirestore.getInstance().collection("postagem")
                            .document(fav.codigoPostagem)
                            .get()
                            .addOnSuccessListener(OnSuccessListener {
                                fav.foto_fav = it.data?.get("fotoUrl").toString()
                                fav.cidade_fav = it.data?.get("cidade").toString()
                                favListAdapter.updateLista(favoritos)
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