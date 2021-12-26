package br.com.equipequatro.traveltips.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.*
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import java.lang.Exception

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnRegistrar.setOnClickListener {
            if(validar()) {
                criarConta()
            }
        }

        return view
    }

    private fun validar(): Boolean {

        var bit_return: Boolean = true

        val email = binding.edtEmail.text.toString()
        val senha = binding.edtPassword.text.toString()
        val senhaConfirmada = binding.edtConfirmPassword.text.toString()

        if (email.isEmpty()) {
            binding.edtEmail.error = getString(R.string.error_email_obrigatorio)
            bit_return = false
        }

        if (senha.isEmpty()) {
            binding.edtPassword.error = getString(R.string.error_senha_obrigatoria)
            bit_return = false
        }

        if (senha != senhaConfirmada) {
            binding.edtConfirmPassword.error = getString(R.string.erro_senha_divergente)
            bit_return = false
        }

        return bit_return
    }

    private fun criarConta() {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
            .addOnSuccessListener(OnSuccessListener {
                binding.edtEmail.setText("")
                binding.edtPassword.setText("")
                binding.edtConfirmPassword.setText("")
                Toast.makeText(context, getString(R.string.usuario_cadastrado), Toast.LENGTH_SHORT).show()
            })
            .addOnFailureListener(OnFailureListener {
                when (it){
                    is FirebaseAuthUserCollisionException
                    -> Toast.makeText(context, getString(R.string.usuario_ja_cadastrado), Toast.LENGTH_SHORT).show()
                    is FirebaseAuthWeakPasswordException
                    -> Toast.makeText(context, getString(R.string.senha_fraca), Toast.LENGTH_SHORT).show()
                    else
                    -> Toast.makeText(context, getString(R.string.error_generico), Toast.LENGTH_SHORT).show()

                }
            })

    }
}