package com.cacd2.cacdgame

import android.os.Parcelable
import com.cacd2.cacdgame.model.Language

actual var isDebug: Boolean = false
actual var isTesting: Boolean = false
actual var KContext: Any? = null
actual var currentLanguage: Language = Language.ENGLISH

actual typealias CommonParcelable = Parcelable
