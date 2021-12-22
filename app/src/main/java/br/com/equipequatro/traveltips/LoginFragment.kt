package br.com.equipequatro.traveltips

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.equipequatro.traveltips.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnLogin.setOnClickListener {

            if (validar()) {

                val loadingDialog = LoadingDialog(this.activity)

                loadingDialog.openDialog()

                val handler = Handler()
                handler.postDelayed({

                    loadingDialog.closeDialog()

                    val intent = Intent(it.context, MainActivity::class.java)
                    startActivity(intent)
                }, 1000)

            } else {
                Toast
                    .makeText(it.context, getString(R.string.msg_campos_obrigatorios), Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.lnkEsqueceuSenha.setOnClickListener {

            val forgotPasswordDialog = ForgotPasswordDialog(this.activity)

            forgotPasswordDialog.openDialog()
        }

        return view
    }

    private fun validar(): Boolean {

        var bit_return: Boolean = true

        if (binding.edtEmail.text.isEmpty()) {
            binding.edtEmail.error = getString(R.string.error_email_obrigatorio)
            bit_return = false
        }

        if (binding.edtPassword.text.isEmpty()) {
            binding.edtPassword.error = "Senha é obrigatório!"
            bit_return = false
        }
        return bit_return

    }
}