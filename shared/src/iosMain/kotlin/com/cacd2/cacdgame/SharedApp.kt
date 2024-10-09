package com.cacd2.cacdgame

import com.cacd2.cacdgame.tools.logger.AppLogger
import com.cacd2.cacdgame.tools.logger.KoinLogger
import org.koin.core.context.startKoin
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
    }
}
