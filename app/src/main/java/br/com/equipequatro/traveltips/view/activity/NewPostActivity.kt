package br.com.equipequatro.traveltips.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.ActivityNewPostBinding
import br.com.equipequatro.traveltips.model.NewPostItem
import br.com.equipequatro.traveltips.repository.NewPostRepository
import br.com.equipequatro.traveltips.util.LoadingDialog
import com.google.firebase.auth.FirebaseAuth

class NewPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewPostBinding
    private var fotoPost: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnSalvar.setOnClickListener {
            if (validar()){
                salvarPostagem()
            }
        }

        binding.btnEscolherFotoPost.setOnClickListener {
            abrirGaleria()
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Escolha uma foto"), 5000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imagem: Intent?) {
        super.onActivityResult(requestCode, resultCode, imagem)

        if (requestCode == 5000 && resultCode == RESULT_OK) {
            val fluxoImagem = contentResolver.openInputStream(imagem!!.data!!)
            fotoPost = BitmapFactory.decodeStream(fluxoImagem)
            binding.imgFotoPost.setImageBitmap(fotoPost)
        }
    }

    fun validar(): Boolean {
        var bit_return: Boolean = true

        if (fotoPost == null) {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Ops!")
            builder.setMessage("A foto é obrigatória!!")
            val dialog: AlertDialog = builder.create()
            dialog.show()

            bit_return = false
        }

        if (binding.txtCidade.text.isEmpty()) {
            binding.txtCidade.error = getString(R.string.error_email_obrigatorio)
            bit_return = false
        }

        if (binding.txtEstado.text.isEmpty()) {
            binding.txtEstado.error = getString(R.string.error_senha_obrigatoria)
            bit_return = false
        }

        if (binding.txtTituloPostagem.text.isEmpty()) {
            binding.txtTituloPostagem.error = getString(R.string.error_senha_obrigatoria)
            bit_return = false
        }

        if (binding.txtDescricaoPostagem.text.isEmpty()) {
            binding.txtDescricaoPostagem.error = getString(R.string.error_senha_obrigatoria)
            bit_return = false
        }

        return bit_return
    }

    fun salvarPostagem() {

        var newPost = NewPostItem (
            cidade = binding.txtCidade.text.toString(),
            dataPostagem = com.google.firebase.Timestamp.now(),
            estado = binding.txtEstado.text.toString(),
            titulo = binding.txtTituloPostagem.text.toString(),
            descricao = binding.txtDescricaoPostagem.text.toString(),
            usuarioCriadorUuid = FirebaseAuth.getInstance().uid.toString()
        )

        val repository = NewPostRepository()
        repository.saveNewPost(
            newPost,
            fotoPost!!,
            this
        )

        val loadingDialog = LoadingDialog(this)

        loadingDialog.openDialog()

        val handler = Handler()
        handler.postDelayed({

            loadingDialog.closeDialog()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 2000)
    }
}