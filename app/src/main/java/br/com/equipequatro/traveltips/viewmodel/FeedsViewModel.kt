package br.com.equipequatro.traveltips.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.equipequatro.traveltips.model.FeedPostItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FeedsViewModel : ViewModel() {

    private var _posts = MutableLiveData<ArrayList<FeedPostItem>>()

    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        listenFeedPostItem()
    }

    // Métodos get e set para Posts
    internal var getFeedPostItem: MutableLiveData<ArrayList<FeedPostItem>>
        get() { return _posts }
        set(value) { _posts.value }

    private fun listenFeedPostItem() {

        val usuarioLogadoId = FirebaseAuth.getInstance().uid

        firestore.collection("postagem")
            .orderBy("dataPostagem", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    return@addSnapshotListener
                }

                val postagens = ArrayList<FeedPostItem>()

                if (snapshot != null) {
                    val documents = snapshot.documents
                    documents.forEach {
                        val posts = it.toObject(FeedPostItem::class.java)
                        postagens.add(posts!!)
                    }
                }
                _posts.value = postagens
            }
    }


}