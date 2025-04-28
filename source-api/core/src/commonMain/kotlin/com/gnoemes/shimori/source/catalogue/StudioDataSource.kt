package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.SStudio

interface StudioDataSource {
    suspend fun getAll(): List<SStudio>

}
