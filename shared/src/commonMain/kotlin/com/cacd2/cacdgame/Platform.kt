package com.cacd2.cacdgame

import com.cacd2.cacdgame.model.Language

expect var isDebug: Boolean
expect var isTesting: Boolean
expect var KContext: Any?
expect var currentLanguage: Language

annotation class CommonParcelize

expect interface CommonParcelable
