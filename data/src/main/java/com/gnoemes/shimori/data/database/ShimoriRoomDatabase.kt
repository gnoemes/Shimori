package com.gnoemes.shimori.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.Rate
import dev.matrix.roomigrant.GenerateRoomMigrations


@Database(
        entities = [
            Anime::class,
            Rate::class
        ],
        version = 1
)
@TypeConverters(ShimoriTypeConverters::class)
@GenerateRoomMigrations
abstract class ShimoriRoomDatabase : RoomDatabase(), ShimoriDatabase