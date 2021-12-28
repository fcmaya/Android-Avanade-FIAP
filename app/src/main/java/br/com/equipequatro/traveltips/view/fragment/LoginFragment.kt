package br.com.equipequatro.traveltips.view.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.FragmentLoginBinding
import br.com.equipequatro.traveltips.repository.SharedPreferencesRepository
import br.com.equipequatro.traveltips.util.ForgotPasswordDialog
import br.com.equipequatro.traveltips.util.LoadingDialog
import br.com.equipequatro.traveltips.view.activity.MainActivity
import com.facebook.*
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.*
import com.google.firebase.internal.api.FirebaseNoSignedInUserException


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    // [START Configuração Google]
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    // [END Configuração Google]

    // [START Configuração Facebook]
    private lateinit var callbackManager: CallbackManager
    private lateinit var btnFacebook: LoginButton
    // [END Configuração Facebook]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [START Configuração Facebook]
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp( this!!.requireActivity()!!.application);
        // [END Configuração Facebook]

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
                autenticar()
            }
        }

        // [START Configuração Google]
        binding.btnGoogle.setSize(SignInButton.SIZE_WIDE);

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

        // [START Configuração Facebook]
        callbackManager = CallbackManager.Factory.create()

        binding.btnFacebook.setReadPermissions("email", "public_profile")
        binding.btnFacebook.setFragment(this);
        binding.btnFacebook.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
            }
        })
        // [END Configuração Facebook]

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
                    val user = auth.currentUser

                    if (user?.displayName == null){
                        SharedPreferencesRepository.savePreferences(context, "displayName", user?.email.toString())
                    }
                    SharedPreferencesRepository.savePreferences(context, "email", user?.email.toString())
                    SharedPreferencesRepository.savePreferences(context, "photoUrl", user?.photoUrl.toString().replace("\\", ""))
                    SharedPreferencesRepository.savePreferences(context, "uid", user?.uid.toString())

                    updateUI(user)
                }
            })
            .addOnFailureListener(OnFailureListener {
                when (it){
                    is FirebaseNoSignedInUserException
                        -> Toast.makeText(context, getString(R.string.usuario_nao_cadastrado), Toast.LENGTH_SHORT).show()
                    is FirebaseAuthInvalidCredentialsException
                        -> Toast.makeText(context, getString(R.string.credenciais_invalidas), Toast.LENGTH_SHORT).show()
                    is FirebaseAuthInvalidUserException
                        -> Toast.makeText(context, getString(R.string.error_usuario_nao_cadastrado), Toast.LENGTH_SHORT).show()
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
            } catch (e: ApiException) {
                Toast.makeText(context, getString(R.string.error_generico), Toast.LENGTH_SHORT).show()
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        SharedPreferencesRepository.savePreferences(context, "displayName", user?.displayName.toString())
                        SharedPreferencesRepository.savePreferences(context, "email", user?.email.toString())
                        SharedPreferencesRepository.savePreferences(context, "photoUrl", user?.photoUrl.toString().replace("\\", ""))
                        SharedPreferencesRepository.savePreferences(context, "uid", user?.uid.toString())

                        updateUI(user)
                    } else {
                        Toast.makeText(context, getString(R.string.autenticacao_falhou),
                            Toast.LENGTH_SHORT).show()
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
        if (user != null) {
            login()
        }
    }
    // [END Configuração Google]

    // [START Configuração Facebook]
    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(context, getString(R.string.autenticacao_falhou),
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
        private const val TAG2 = "FacebookLogin"
    }
    // [END Configuração Facebook]
}