package br.com.equipequatro.traveltips.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.FragmentProfileBinding
import br.com.equipequatro.traveltips.model.Perfil
import br.com.equipequatro.traveltips.repository.SharedPreferencesRepository
import br.com.equipequatro.traveltips.util.ForgotPasswordDialog
import br.com.equipequatro.traveltips.viewmodel.FragmentProfileViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel : FragmentProfileViewModel
    private var nome = ""
    private var email = ""
    private var uid = ""
    private var fotoUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        mViewModel = ViewModelProvider.NewInstanceFactory().create(FragmentProfileViewModel::class.java)

        carregarDados()

        binding.btnEditarAtualizar.setOnClickListener {
            if (validar()){
                salvar()
            }
        }

        binding.btnEditarRedefinir.setOnClickListener {
            val forgotPasswordDialog = ForgotPasswordDialog(this.activity)
            forgotPasswordDialog.openDialog()
        }

        return view
    }

    private fun salvar() {
        val perfil = Perfil(uid, nome, email, fotoUrl)

        FirebaseFirestore.getInstance().collection("perfil")
            .add(perfil)
            .addOnSuccessListener(OnSuccessListener {
                Toast.makeText(context, getString(R.string.usuario_cadastrado), Toast.LENGTH_SHORT).show()

            })
            .addOnFailureListener(OnFailureListener {
                Toast.makeText(context, getString(R.string.error_generico), Toast.LENGTH_SHORT).show()
            })
    }

    private fun carregarDados() {

        nome     = SharedPreferencesRepository.getPreferences(context, "displayName")!!
        email    = SharedPreferencesRepository.getPreferences(context, "email")!!
        uid      = SharedPreferencesRepository.getPreferences(context, "uid")!!
        fotoUrl  = SharedPreferencesRepository.getPreferences(context, "photoUrl")!!

        binding.etEditarNome.setText(nome)
        binding.etEditarEmail.setText(email)
        Glide.with(this).load(fotoUrl).into(binding.ivEditarFoto);

    }

    fun validar(): Boolean {

        var bit_return: Boolean = true

        val nome = binding.etEditarNome.text.toString()
        val email = binding.etEditarEmail.text.toString()

        if (nome.isEmpty()) {
            binding.etEditarNome.error = getString(R.string.error_nome_obrigatorio)
            bit_return = false
        }

        if (email.isEmpty()) {
            binding.etEditarEmail.error = getString(R.string.error_email_obrigatorio)
            bit_return = false
        }

        return bit_return
    }
}

