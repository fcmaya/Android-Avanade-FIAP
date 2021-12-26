package br.com.equipequatro.traveltips.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import br.com.equipequatro.traveltips.view.activity.MainActivity
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.FragmentLoginBinding
import br.com.equipequatro.traveltips.util.ForgotPasswordDialog
import br.com.equipequatro.traveltips.util.LoadingDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import java.lang.Exception


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    // [START Configuração Google]
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    // [END Configuração Google]

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

//        val usuarioAutenticado = FirebaseAuth.getInstance().currentUser
//        if (usuarioAutenticado != null) {
//            login()
//        }

        binding.btnLogin.setOnClickListener {
            if (validar()) {
                autenticar()
            }
        }

        // [START Configuração Google]
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_android_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this.activity, gso)
        auth = FirebaseAuth.getInstance()

        binding.btnGoogle.setOnClickListener {
            signIn()
        }
        // [END Configuração Google]

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
            binding.edtPassword.error = getString(R.string.error_senha_obrigatoria)
            bit_return = false
        }
        return bit_return
    }

    private fun autenticar(): Boolean {

        var bit_return: Boolean = false

        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful){
                    login()
                }
            })
            .addOnFailureListener(OnFailureListener {
                when (it){
                    is FirebaseNoSignedInUserException
                        -> Toast.makeText(context, getString(R.string.usuario_nao_cadastrado), Toast.LENGTH_SHORT).show()
                    is FirebaseAuthInvalidCredentialsException
                        -> Toast.makeText(context, getString(R.string.credenciais_invalidas), Toast.LENGTH_SHORT).show()
                    else
                        -> Toast.makeText(context, getString(R.string.error_generico), Toast.LENGTH_SHORT).show()
                }
            })

        return bit_return
    }

    private fun login() {

        val loadingDialog = LoadingDialog(this.activity)

        loadingDialog.openDialog()

        val handler = Handler()
        handler.postDelayed({

            loadingDialog.closeDialog()

            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }, 1000)

    }


    // [START Configuração Google]
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                login()
            } catch (e: ApiException) {
                Toast.makeText(context, getString(R.string.error_generico), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        updateUI(null)
                    }
                }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
    // [END Configuração Google]
}