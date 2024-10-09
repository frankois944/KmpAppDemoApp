package com.cacd2.cacdgame

/**
 * Created by francois.dabonot@cacd2.fr on 04/05/2023.
 */
object Constants {
    const val DATO_CMS_API_KEY = "de80e1d21d4d06526ff064c0fcb806"
    const val API_KEY = "d12d8f42-bc2b-4ec9-b1aa-e8705f4ff848"
    const val EMAIL_REGEX =
        "^(?=.{1,254}$)(?=.{1,64}@)[-!#$%&'*+0-9=?A-Z^_`a-z{|}~]" +
            "+(.[-!#$%&'*+0-9=?A-Z^_`a-z{|}~]+)*@[A-Za-z0-9]([A-Za-z0-9-]" +
            "{0,61}[A-Za-z0-9])?(.[A-Za-z0-9]([A-Za-z0-9-]{0,61}[A-Za-z0-9])?)*$"
}
