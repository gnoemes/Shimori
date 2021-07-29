package com.gnoemes.shimori.model.app

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.rate.RateTargetType

@Entity(
        tableName = "pinned",
        indices = [
            Index(value = ["target_type"])
        ]
)
data class ListPin(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "target_id") val targetId: Long? = null,
    @ColumnInfo(name = "target_type") val targetType: RateTargetType
) : ShimoriEntity