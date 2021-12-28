package br.com.equipequatro.traveltips.model

import com.google.firebase.Timestamp

data class FeedPostItem(
    var id_post: String = "",
    var codigo_user: String = "",
    var nome_user: String = "",
    var foto_user: String = "",
    var cidade_post: String = "",
    var estado_post: String = "",
    var legenda_post: String = "",
    var descricao_post: String = "",
    var favoritado: Boolean = false,
    var id_favoritado: String = "",
    var num_comentarios: String = "",
    var foto_url_post: String = "",
    var data_post: Timestamp = Timestamp(0, 0)
)
