package com.cacd2.cacdgame.datasource.api.game

/**
 * Created by francois.dabonot@cacd2.fr on 24/05/2023.
 */
sealed class DatoCMSAPIError(source: Exception) : Exception(source) {
    data class NetworkError(val source: Exception) : DatoCMSAPIError(source)

    data class ApiError(val source: Exception) : DatoCMSAPIError(source)
}
