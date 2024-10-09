package com.cacd2.cacdgame.datasource.api.new

import kotlinx.serialization.Serializable

@Serializable
data class NewLeadRequestObject(
    val firstname: String,
    val lastname: String,
    val email: String,
    val phoneNumber: String?,
    val comment: String?
)
