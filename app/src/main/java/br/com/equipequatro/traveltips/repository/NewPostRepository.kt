package br.com.equipequatro.traveltips.repository

import android.content.Context
import android.graphics.Bitmap
import br.com.equipequatro.traveltips.model.FeedPostItem
import br.com.equipequatro.traveltips.model.NewPostItem
import br.com.equipequatro.traveltips.util.converterBitmapToByteArray
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDate

class NewPostRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().getReference("postagem")

    fun saveNewPost(newPost: NewPostItem, fotoPost: Bitmap, context: Context) {
        var db = FirebaseFirestore.getInstance()

        db.collection("postagem").add(newPost)
            .addOnSuccessListener {
                var codigoPostagem = it.id
                var nomeFoto = "$codigoPostagem.jpg"

                val fotoPerfilRef = storage.child(nomeFoto)

                fotoPerfilRef.putBytes(converterBitmapToByteArray(fotoPost))
                    .addOnSuccessListener(OnSuccessListener {
                        fotoPerfilRef.downloadUrl.addOnSuccessListener(OnSuccessListener {
                            firestore.collection("postagem")
                                .document(codigoPostagem)
                                .update("fotoUrl", it.toString())
                        })
                    })
            }
    }

}