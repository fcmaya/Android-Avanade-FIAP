package br.com.equipequatro.traveltips.model

import java.time.LocalDate
import java.util.*

data class NewImagePostItem(
    var id_post: Int = 0,
    var nome_user: String = "",
    var cidade_user: String = "",
    var legenda_post: String = "",
    var descricao_post: String = "",
    var favorito: Boolean = false,
    var num_comentarios: Int = 0,
    var data_post: LocalDate = LocalDate.now()
)
