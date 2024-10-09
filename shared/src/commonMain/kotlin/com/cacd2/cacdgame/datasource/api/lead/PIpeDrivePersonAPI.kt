package com.cacd2.cacdgame.datasource.api.lead

import com.cacd2.cacdgame.datasource.api.base.FailureBodyObject
import com.cacd2.cacdgame.datasource.api.base.SuccessBodyObject
import com.cacd2.cacdgame.datasource.api.new.NewLeadRequestObject
import com.cacd2.cacdgame.datasource.api.new.NewLeadResponseObject
import com.cacd2.cacdgame.tools.logger.AppLogger
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

/**
 * Created by francois.dabonot@cacd2.fr on 04/05/2023.
 */
suspend fun PipeDriveAPI.CreateLead(query: NewLeadRequestObject): Result<NewLeadResponseObject> {
    return try {
        val response =
            httpClient.post(urlString = "lead/new") {
                setBody(query)
            }
        parseResponse(response)
    } catch (ex: Exception) {
        AppLogger.e("CreateLead BAD REQUEST", ex)
        Result.failure(Exception(ex))
    }
}

private suspend inline fun <reified T> parseResponse(response: HttpResponse): Result<T> {
    return try {
        if (response.status.isSuccess()) {
            Result.success(response.body<SuccessBodyObject<T>>().data)
        } else {
            Result.failure(Exception(response.body<FailureBodyObject>().message))
        }
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
