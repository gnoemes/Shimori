package com.gnoemes.shimori.data.database

import androidx.room.withTransaction
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val db: ShimoriRoomDatabase
) : DatabaseTransactionRunner {
    override suspend fun <T> invoke(block: suspend () -> T): T = db.withTransaction {
        block()
    }

}