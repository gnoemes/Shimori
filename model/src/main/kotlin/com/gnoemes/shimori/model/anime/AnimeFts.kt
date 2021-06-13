package com.gnoemes.shimori.model.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = Anime::class)
@Entity(tableName = "animes_fts")
data class AnimeFts(
    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE) val name: String? = null,
    @ColumnInfo(name = "name_ru_lower_case") val nameRu: String? = null
)