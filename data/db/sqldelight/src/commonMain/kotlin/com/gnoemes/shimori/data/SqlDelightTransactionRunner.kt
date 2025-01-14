package com.gnoemes.shimori.data

import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = DatabaseTransactionRunner::class)
class SqlDelightTransactionRunner(
    private val db: ShimoriDB
) : DatabaseTransactionRunner {
    override fun <T> invoke(block: () -> T): T {
        return db.transactionWithResult {
            block()
        }
    }
}