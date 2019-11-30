package com.gnoemes.shimori.data.daos

import androidx.room.Dao
import com.gnoemes.shimori.model.anime.Anime

@Dao
abstract class AnimeDao : EntityDao<Anime> {

}