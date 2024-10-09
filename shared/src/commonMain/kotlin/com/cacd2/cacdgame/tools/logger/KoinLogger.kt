package com.cacd2.cacdgame.tools.logger

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

/**
 * Created by francois.dabonot@cacd2.fr on 03/05/2023.
 */
class KoinLogger : Logger() {
    override fun display(level: Level, msg: MESSAGE) {
        when (level) {
            Level.DEBUG -> AppLogger.d(msg)
            Level.INFO -> AppLogger.i(msg)
            Level.WARNING -> AppLogger.w(msg)
            Level.ERROR -> AppLogger.e(msg)
            Level.NONE -> {}
        }
    }
}
