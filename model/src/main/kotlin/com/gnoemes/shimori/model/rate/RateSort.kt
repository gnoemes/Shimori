package com.gnoemes.shimori.model.rate

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gnoemes.shimori.model.ShimoriEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
        tableName = "rate_sort",
        indices = [
            Index(value = ["type", "status"], unique = true)
        ]
)
data class RateSort(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val type: RateTargetType? = null,
    val status: RateStatus? = null,
    @SerializedName("sort") val sortOption: RateSortOption = RateSortOption.NAME,
    @SerializedName("descending") val isDescending: Boolean = false
) : ShimoriEntity, Serializable {

    companion object {
        fun default() = RateSort(
                type = RateTargetType.ANIME,
                status = RateStatus.WATCHING,
                sortOption = RateSortOption.NAME,
                isDescending = false
        )
    }
}