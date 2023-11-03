package com.gnoemes.shimori.data

import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import me.tatarka.inject.annotations.Inject

@Inject
class SqlDelightTransactionRunner(
    private val db: ShimoriDB
) : DatabaseTransactionRunner {
    override fun <T> invoke(block: () -> T): T {
        return db.transactionWithResult {
            block()
        }
    }
}