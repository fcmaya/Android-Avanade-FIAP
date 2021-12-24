package br.com.equipequatro.traveltips.repository

import br.com.equipequatro.traveltips.model.FeedPostItem
import java.time.LocalDate

class FeedPostItemRepository {

    companion object {

        fun getPostItem(): List<FeedPostItem> {
            val posts = mutableListOf<FeedPostItem>(
                FeedPostItem(1, "Rafael Silva", "São Paulo", "Mar verde e lindo", "Esse dia blablablablabla foi blablablabla", false, 5, LocalDate.of(2021, 10, 2)),
                FeedPostItem(2, "Maria Carvalho", "Ubatuba", "Linda paisagem", "Sempre que venho blablablablabla foi blablablabla", false, 6, LocalDate.of(2021, 11, 20)),
                FeedPostItem(3, "Jéssica Domingues", "Ilhabela", "Dia lindo na praia", "Aqui nesse dia blablablablabla foi blablablabla", false, 6, LocalDate.of(2021, 10, 12)),
                FeedPostItem(4, "Mário Costa Silva", "São Paulo", "Momento de paz", "Hoje blablablablabla foi blablablabla", false, 50, LocalDate.of(2021, 10, 21)),
                FeedPostItem(5, "José Aparecido", "São Joaquim da Barra", "Sol e chuva", "Sai para blablablablabla foi blablablabla", false, 15, LocalDate.of(2021, 9, 10)),
                FeedPostItem(6, "Miralva Rodrigo Pereira", "Fortaleza", "Azul céu", "Sempre que blablablablabla foi blablablabla", false, 2, LocalDate.of(2021, 7, 2)),
                FeedPostItem(7, "Amanda Ap Albuquerque", "Santos", "Dia tranquilo", "Além disso blablablablabla foi blablablabla", false, 1, LocalDate.of(2021, 1, 22)),
                FeedPostItem(8, "Leonardo Ott", "Araraquara", "Ondas e o mar", "E Nunca blablablablabla foi blablablabla", false, 0, LocalDate.of(2021, 10, 25)),
                FeedPostItem(9, "Priscila Domingues", "São Vicente", "Mais bela cachoeira", "Hoje é o meu blablablablabla foi blablablabla", false, 0, LocalDate.of(2021, 12, 11)),
                FeedPostItem(10, "Larissa Silva", "Florianopólis", "Praia translúcida e linda", "Quão é blablablablabla foi blablablabla", false, 12, LocalDate.of(2021, 12, 13))
            )
            return posts
        }

    }
}