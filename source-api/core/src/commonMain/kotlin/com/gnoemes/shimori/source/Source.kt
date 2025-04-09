package com.gnoemes.shimori.source

import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.model.SourceValues

interface Source {
    /**
     * Must be unique
     */
    val id: Long

    val name: String

    val values: SourceValues

    val availableData: List<SourceDataType>
    val supportsMalIds: Boolean get() = malIdsSupport.isNotEmpty()

    val malIdsSupport: List<SourceDataType> get() = emptyList()

    val languages: List<String> get() = emptyList()
}