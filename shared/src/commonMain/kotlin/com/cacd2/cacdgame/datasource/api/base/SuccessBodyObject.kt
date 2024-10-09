package com.cacd2.cacdgame.datasource.api.base

import kotlinx.serialization.Serializable

@Serializable
data class SuccessBodyObject<out T>(val data: T)
