package br.com.equipequatro.traveltips.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.databinding.ActivityNewPostBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.DateTime
import java.sql.Timestamp
import java.time.LocalDateTime


class NewPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewPostBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.btnSalvar.setOnClickListener {
            salvarPostagem()
        }

    }

    fun salvarPostagem(){

        val data1 = hashMapOf(
            "cidade" to "Manaus",
            "dataPostagem" to com.google.firebase.Timestamp.now(),
            "descricao" to "Um bom lugar para se estar depois de uma semana cansativa",
            "estado" to "Amazonas",
            "fotoUrl" to "https://firebasestorage.googleapis.com/v0/b/avanade-fiap-maia.appspot.com/o/praia.jpg?alt=media&token=9f1b49a2-f81a-4d58-897b-77d63a726b3a",
            "titulo" to "Teste Manaus",
            "usuarioCriadorUuid" to "QwF5R970indgTy1lOUbFclaET0d2"
        )

        var db = FirebaseFirestore.getInstance()

        db.collection("postagem").document().set(data1)

    }
}