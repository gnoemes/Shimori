package com.gnoemes.shimori.tasks

import android.content.Context
import androidx.work.WorkerParameters

internal data class WorkerPayload(
    val context: Context,
    val params : WorkerParameters
)