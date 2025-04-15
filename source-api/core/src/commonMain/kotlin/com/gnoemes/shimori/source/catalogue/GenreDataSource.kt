package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.SGenre

interface GenreDataSource {
    suspend fun getAll(): List<SGenre>
}