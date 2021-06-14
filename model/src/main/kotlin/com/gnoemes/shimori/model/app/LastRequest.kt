package com.gnoemes.shimori.model.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gnoemes.shimori.model.ShimoriEntity
import org.threeten.bp.Instant

@Entity(
        tableName = "last_requests",
        indices = [
            Index(value = ["request", "entity_id"], unique = true)
        ]
)
data class LastRequest(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val request: Request,
    @ColumnInfo(name = "entity_id") val entityId: Long,
    val timestamp: Instant
) : ShimoriEntity