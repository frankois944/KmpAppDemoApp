package com.cacd2.cacdgame.android

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import com.cacd2.cacdgame.SharedApp
import com.cacd2.cacdgame.isDebug
import com.cacd2.cacdgame.isTesting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainApplication : Application() {
    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    // .penaltyDeath()
                    .build()
            )
        }
        super.onCreate()
        if (BuildConfig.FOR_TESTING) {
            /*Shake.getReportConfiguration().isShowFloatingReportButton = true
            Shake.getReportConfiguration().isAutoVideoRecording = true
            Shake.getReportConfiguration().isSensitiveDataRedactionEnabled = false
            Shake.start(
                this,
                "MV6iYId9D7ONdjKxt9OI0kSoLTYz8co9crNOsRhR",
                "0TqfIE6nNFtowGTxxee67qq2JJexFkrYysugUtptht6DJHKMTc049hB"
            )*/
        }
        runBlocking(Dispatchers.Default) {
            isTesting = BuildConfig.FOR_TESTING
            isDebug = BuildConfig.DEBUG
            SharedApp.start(context = applicationContext)
        }
    }
}
