package com.gnoemes.shimori.appinitializers

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import java.util.concurrent.Executor
import javax.inject.Inject

class ArchInitializer @Inject constructor(
    private val backgroundExecutor: Executor
) : AppInitializer {

    override fun init(app: Application) {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            @Volatile
            private var mainHandler: Handler? = null
            private val lock = Any()

            override fun executeOnDiskIO(runnable: Runnable) {
                backgroundExecutor.execute(runnable)
            }

            override fun postToMainThread(runnable: Runnable) {
                if (mainHandler == null) {
                    synchronized(lock) {
                        if (mainHandler == null) {
                            mainHandler = Handler(Looper.getMainLooper())
                        }
                    }
                }
                mainHandler?.post(runnable)
            }

            override fun isMainThread(): Boolean {
                return Looper.getMainLooper().thread === Thread.currentThread()
            }
        })
    }
}