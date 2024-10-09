package com.cacd2.cacdgame.datasource.api.base

import kotlinx.serialization.Serializable

@Serializable
data class FailureBodyObject(val message: String) {
    companion object {
        fun from(ex: Throwable): FailureBodyObject {
            return FailureBodyObject(ex.message!!)
        }
    }
}
