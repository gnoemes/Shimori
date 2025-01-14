package com.gnoemes.shimori.data.db.api.db

interface DatabaseTransactionRunner {
    operator fun <T> invoke(block: () -> T): T
}