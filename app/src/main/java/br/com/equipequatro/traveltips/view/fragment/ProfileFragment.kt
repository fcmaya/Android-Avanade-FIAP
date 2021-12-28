package br.com.equipequatro.traveltips.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.FragmentProfileBinding
import br.com.equipequatro.traveltips.viewmodel.FragmentProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel : FragmentProfileViewModel

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

        binding.btnEditarAtualizar.setOnClickListener {
            if (validar()){

            }
        }

        return view
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

