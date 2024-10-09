package com.cacd2.cacdgame.model

import com.cacd2.cacdgame.datasource.api.lead.CreateLead
import com.cacd2.cacdgame.datasource.api.lead.PipeDriveAPI
import com.cacd2.cacdgame.datasource.api.new.NewLeadRequestObject

/**
 * Created by francois.dabonot@cacd2.fr on 01/06/2023.
 */
object Contact {
    suspend fun createLead(
        firstname: String,
        lastname: String,
        email: String,
        comment: String,
        phoneNumber: String?
    ): Boolean {
        return PipeDriveAPI.CreateLead(
            NewLeadRequestObject(
                firstname,
                lastname,
                email,
                phoneNumber,
                comment
            )
        ).isSuccess
    }
}
