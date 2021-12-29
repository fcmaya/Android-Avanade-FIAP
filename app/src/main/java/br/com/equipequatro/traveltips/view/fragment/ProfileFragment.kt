package br.com.equipequatro.traveltips.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Transition
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.ActivityNewPostBinding
import br.com.equipequatro.traveltips.databinding.FragmentProfileBinding
import br.com.equipequatro.traveltips.model.Perfil
import br.com.equipequatro.traveltips.model.UsuarioItem
import br.com.equipequatro.traveltips.repository.SharedPreferencesRepository
import br.com.equipequatro.traveltips.util.ForgotPasswordDialog
import br.com.equipequatro.traveltips.util.LoadingDialog
import br.com.equipequatro.traveltips.util.converterBitmapToByteArray
import br.com.equipequatro.traveltips.view.activity.MainActivity
import br.com.equipequatro.traveltips.viewmodel.FragmentProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private val storage = FirebaseStorage.getInstance().getReference("usuario")
    private val firestore = FirebaseFirestore.getInstance()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: FragmentProfileViewModel
    private var nome = ""
    private var email = ""
    private var uid = ""
    private var fotoUrl = ""
    private var fotoPerfil: Bitmap? = null

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

        mViewModel =
            ViewModelProvider.NewInstanceFactory().create(FragmentProfileViewModel::class.java)

        carregarDados()

        binding.btnEditarAtualizar.setOnClickListener {
            if (validar()) {
                salvar()
            }
        }

        binding.btnSelecionarFotoPerfil.setOnClickListener {
            abrirGaleria()
        }

        binding.btnEditarRedefinir.setOnClickListener {
            val forgotPasswordDialog = ForgotPasswordDialog(this.activity)
            forgotPasswordDialog.openDialog()
        }

        return view
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Escolha uma foto"), 5000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imagem: Intent?) {
        super.onActivityResult(requestCode, resultCode, imagem)

        if (requestCode == 5000 && resultCode == AppCompatActivity.RESULT_OK) {
            val fluxoImagem = context?.contentResolver?.openInputStream(imagem!!.data!!)
            fotoPerfil = BitmapFactory.decodeStream(fluxoImagem)
            binding.ivEditarFoto.setImageBitmap(fotoPerfil)
            binding.root.findViewById<ImageView>(R.id.iv_editar_foto)?.setImageBitmap(fotoPerfil)
        }
    }

    private fun salvar() {
        val usuario = UsuarioItem(
            nome = binding.etEditarNome.text.toString(),
            email = binding.etEditarEmail.text.toString(),
            uuid = FirebaseAuth.getInstance().currentUser!!.uid
        )

        var db = FirebaseFirestore.getInstance()

        db.collection("usuario")
            .add(usuario)
            .addOnSuccessListener(OnSuccessListener {

                var codigoUsuario = it.id
                var nomeFoto = "$codigoUsuario.jpg"

                val fotoPerfilRef = storage.child(nomeFoto)

                fotoPerfilRef.putBytes(converterBitmapToByteArray(fotoPerfil!!))
                    .addOnSuccessListener(OnSuccessListener {
                        fotoPerfilRef.downloadUrl.addOnSuccessListener(OnSuccessListener {
                            firestore.collection("usuario")
                                .document(codigoUsuario)
                                .update("fotoPerfilUrl", it.toString())
                        })
                    })

                Toast.makeText(context, getString(R.string.usuario_atualizado), Toast.LENGTH_SHORT)
                    .show()

            })
            .addOnFailureListener(OnFailureListener {
                Toast.makeText(context, getString(R.string.error_generico), Toast.LENGTH_SHORT)
                    .show()
            })
    }

    private fun carregarDados() {

        FirebaseFirestore.getInstance().collection("usuario")
            .whereEqualTo("uuid", FirebaseAuth.getInstance().currentUser?.uid)
            .get()
            .addOnSuccessListener(OnSuccessListener {
                if (it != null) {

                    for (user in it.documents) {
                        if (user.data?.get("nome") != null) {
                            binding.etEditarNome.setText(user.data?.get("nome").toString())
                        }

                        if (user.data?.get("email") != null) {
                            binding.etEditarEmail.setText(user.data?.get("email").toString())
                        } else {
                            if (FirebaseAuth.getInstance().currentUser?.email != "") {
                                binding.etEditarEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
                            }
                        }

                        if (user.data?.get("fotoPerfilUrl") != null) {
                            Glide.with(this)
                                .load(user.data?.get("fotoPerfilUrl").toString())
                                .into(binding.ivEditarFoto)
                        } else {
                            if (FirebaseAuth.getInstance().currentUser?.photoUrl != null) {
                                Glide.with(this)
                                    .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
                                    .into(binding.ivEditarFoto)
                            }
                        }
                    }
                } else {

                    nome = SharedPreferencesRepository.getPreferences(context, "displayName")!!
                    email = SharedPreferencesRepository.getPreferences(context, "email")!!
                    uid = SharedPreferencesRepository.getPreferences(context, "uid")!!
                    fotoUrl = SharedPreferencesRepository.getPreferences(context, "photoUrl")!!

                    binding.etEditarNome.setText(nome)
                    binding.etEditarEmail.setText(email)

                    if (fotoUrl != "") {
                        // Glide.with(this).load(fotoUrl).into(binding.ivEditarFoto)

                        //var bitmap = Glide.with(this).asBitmap().load(fotoUrl) //.into(binding.ivEditarFoto);
                        //binding.root.findViewById<ImageView>(R.id.iv_editar_foto)?.setImageBitmap(bitmap)
                    }

                }

            })


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

