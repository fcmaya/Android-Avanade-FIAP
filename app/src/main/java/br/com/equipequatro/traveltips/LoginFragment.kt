package br.com.equipequatro.traveltips

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class LoginFragment : Fragment() {

    private lateinit var editLogin: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button

    private lateinit var btnGoogle: Button
    private lateinit var btnFacebook: Button

    private lateinit var lnkForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_login, container, false)

        editLogin = view.findViewById<EditText>(R.id.edit_email)
        editPassword = view.findViewById<EditText>(R.id.edit_password)
        btnLogin = view.findViewById<Button>(R.id.btn_recuperar)

        btnGoogle = view.findViewById<Button>(R.id.vt_btn_google)
        btnFacebook = view.findViewById<Button>(R.id.vt_btn_fb)

        lnkForgotPassword = view.findViewById<TextView>(R.id.link_forgot_password)

        btnLogin.setOnClickListener {
            Log.d("botao", "Login")

            if (validar()) {

                val loadingDialog = LoadingDialog(this.activity)

                loadingDialog.openDialog()

                val handler = Handler()
                handler.postDelayed({

                    loadingDialog.closeDialog()

                    val intent = Intent(it.context, MainActivity::class.java)
                    startActivity(intent)
                }, 5000)

            } else {
                Toast
                    .makeText(it.context, "Ops! Campos obrigatórios", Toast.LENGTH_LONG)
                    .show()
            }
        }

        lnkForgotPassword.setOnClickListener {
            Log.d("botao", "esqueci a senha")

            val forgotPasswordDialog = ForgotPasswordDialog(this.activity)

            forgotPasswordDialog.openDialog()
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun validar(): Boolean {

        var bit_return: Boolean = true

        if (editLogin.text.isEmpty()) {
            editLogin.error = "E-mail é obrigatório!"
            bit_return = false
        }

        if (editPassword.text.isEmpty()) {
            editPassword.error = "Senha é obrigatório!"
            bit_return = false
        }

        return bit_return

    }
}