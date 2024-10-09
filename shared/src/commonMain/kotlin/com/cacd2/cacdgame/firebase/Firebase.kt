package com.cacd2.cacdgame.firebase

import com.cacd2.cacdgame.datasource.settings.ConsentManagerData

/**
 * Created by francois.dabonot@cacd2.fr on 03/07/2023.
 */
expect object Firebase {
    fun initFirebase(context: Any?)

    fun toggleAnalytics(consent: ConsentManagerData)
}
