package com.cacd2.cacdgame.tools.logger

import co.touchlab.kermit.ExperimentalKermitApi
import co.touchlab.kermit.Logger
import co.touchlab.kermit.NoTagFormatter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.cacd2.cacdgame.isDebug
import com.cacd2.cacdgame.isTesting

/**
 * Created by francois.dabonot@cacd2.fr on 06/04/2023.
 */
@OptIn(ExperimentalKermitApi::class)
object AppLogger : Logger(
    config =
    loggerConfigInit(
        logWriters =
        (
            if (isTesting) {
                if (isDebug) {
                    listOf(
                        platformLogWriter(NoTagFormatter)
                    )
                } else {
                    listOf(
                        CrashlyticsLogWriter(
                            minCrashSeverity = Severity.Verbose,
                            minSeverity = Severity.Verbose
                        ),
                        platformLogWriter(NoTagFormatter)
                    )
                }
            } else {
                listOf(
                    CrashlyticsLogWriter(
                        minCrashSeverity = Severity.Warn,
                        minSeverity = Severity.Debug
                    )
                )
            }
            ).toTypedArray(),
        minSeverity =
        if (isTesting) {
            Severity.Verbose
        } else {
            Severity.Debug
        }
    ),
    tag = "CACDGAME"
)
