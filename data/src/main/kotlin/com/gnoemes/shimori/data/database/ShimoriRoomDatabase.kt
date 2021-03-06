package com.gnoemes.shimori.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeFts
import com.gnoemes.shimori.model.app.LastRequest
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.user.User


@Database(
        entities = [
            Anime::class,
            AnimeFts::class,
            Rate::class,
            LastRequest::class,
            User::class,
            RateSort::class
        ],
        version = 1
)
@TypeConverters(ShimoriTypeConverters::class)
abstract class ShimoriRoomDatabase : RoomDatabase(), ShimoriDatabase