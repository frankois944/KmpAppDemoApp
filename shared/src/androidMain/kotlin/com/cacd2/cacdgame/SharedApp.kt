package com.cacd2.cacdgame

import android.content.Context
import com.cacd2.cacdgame.tools.logger.AppLogger
import com.cacd2.cacdgame.tools.logger.KoinLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.defaultModule

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */
actual fun startPlatform() {
    AppLogger.i("setupInjection")
    setupInjection()
}

private fun setupInjection() {
    startKoin {
        printLogger(
            if (isDebug) {
                Level.INFO
            } else {
                Level.NONE
            }
        )
        logger(KoinLogger())
        modules(
            defaultModule
        )
        androidContext(KContext as Context)
    }
}
