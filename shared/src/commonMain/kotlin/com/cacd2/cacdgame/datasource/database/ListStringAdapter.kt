package com.cacd2.cacdgame.datasource.database

import app.cash.sqldelight.ColumnAdapter

/**
 * Created by francois.dabonot@cacd2.fr on 24/05/2023.
 */
val listOfStringsAdapter =
    object : ColumnAdapter<List<String>, String> {
        override fun decode(databaseValue: String) = if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(",")
        }

        override fun encode(value: List<String>) = value.joinToString(separator = ",")
    }
