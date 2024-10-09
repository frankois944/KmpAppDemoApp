package com.cacd2.cacdgame.datasource.api.game

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.cacd2.cacdgame.Constants
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.hasContentInDB
import com.cacd2.cacdgame.datasource.database.dao.saveQuestions
import com.cacd2.cacdgame.datasource.settings.AppSettingsKeys
import com.cacd2.cacdgame.datasource.settings.SettingsDataSource
import com.cacd2.cacdgame.graphql.AllDataFromDateQuery
import com.cacd2.cacdgame.graphql.type.SiteLocale
import com.cacd2.cacdgame.model.Language
import com.cacd2.cacdgame.tools.logger.AppLogger
import com.cacd2.cacdgame.updateCallHoursInterval
import com.cacd2.cacdgame.updateDataHoursInterval
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until

/**
 * Created by francois.dabonot@cacd2.fr on 19/04/2023.
 */
object DatoCMSAPI {
    private fun getClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://graphql.datocms.com/")
            .addHttpHeader("Authorization", "bearer ${Constants.DATO_CMS_API_KEY}")
            .addHttpHeader("Accept-Encoding", "*")
            .fetchPolicy(FetchPolicy.NetworkOnly)
            .build()
    }

    private suspend fun getAllDataQueryFromDate(
        language: Language
    ): ApolloCall<AllDataFromDateQuery.Data> {
        AppLogger.d("Get Query for fetch all Categories and language $language")
        val lastUpdate =
            if (language == Language.FRENCH) {
                AppSettingsKeys.LAST_DATA_UPDATE_DATETIME_FR
            } else {
                AppSettingsKeys.LAST_DATA_UPDATE_DATETIME_EN
            }
        val lastUpdateTimestamp = SettingsDataSource.get(lastUpdate)?.toLong() ?: 0L
        val date = Instant.fromEpochMilliseconds(lastUpdateTimestamp).toString()
        return if (language == Language.FRENCH) {
            getClient().query(
                AllDataFromDateQuery(Optional.present(SiteLocale.fr), Optional.present(date))
            )
        } else {
            getClient().query(
                AllDataFromDateQuery(Optional.present(SiteLocale.en), Optional.present(date))
            )
        }
    }

    suspend fun updateModeFor(language: Language): UpdateMode {
        if (!Database.data.hasContentInDB(language)) {
            AppLogger.d(
                "Full Update [NO DATA] for $language"
            )
            return UpdateMode.FULL
        }
        val currentTime = Clock.System.now()
        val lastCall =
            if (language == Language.FRENCH) {
                AppSettingsKeys.LAST_CALL_DATA_FETCH_DATETIME_FR
            } else {
                AppSettingsKeys.LAST_CALL_DATA_FETCH_DATETIME_EN
            }
        val lastCallDate =
            Instant.fromEpochMilliseconds(
                SettingsDataSource.get(lastCall)?.toLong() ?: 0L
            )
        val lastUpdate =
            if (language == Language.FRENCH) {
                AppSettingsKeys.LAST_DATA_UPDATE_DATETIME_FR
            } else {
                AppSettingsKeys.LAST_DATA_UPDATE_DATETIME_EN
            }
        val lastUpdateDate =
            Instant.fromEpochMilliseconds(
                SettingsDataSource.get(lastUpdate)?.toLong() ?: 0L
            )
        if (currentTime < lastCallDate || currentTime < lastUpdateDate) {
            AppLogger.d(
                """
                Full Update for $language lastCallTimestamp:$lastCallDate - lastUpdateTimestamp:$lastUpdateDate"
                """.trimIndent()
            )
            return UpdateMode.FULL
        }
        // wait 24 hours before updating data between 12H of try
        val diffInHourForCall =
            lastCallDate.until(
                Clock.System.now(),
                DateTimeUnit.HOUR,
                TimeZone.UTC
            )
        val diffInHourForUpdate =
            lastUpdateDate.until(
                Clock.System.now(),
                DateTimeUnit.HOUR,
                TimeZone.UTC
            )
        if (diffInHourForUpdate >= updateDataHoursInterval &&
            diffInHourForCall >= updateCallHoursInterval
        ) {
            AppLogger.d(
                """
                Silent Update for $language
                Reason:
                lastcall $diffInHourForUpdate > ${updateDataHoursInterval}h
                lastupdate $diffInHourForCall > ${updateCallHoursInterval}h
                """.trimIndent()
            )
            return UpdateMode.SILENT
        }
        AppLogger.d("No Update needed for $language")
        return UpdateMode.NOTNEEDED
    }

    suspend fun fetchAllData(language: Language? = null) {
        AppLogger.d("Start fetch all Categories")
        var onError: DatoCMSAPIError? = null
        try {
            if (language != null) {
                SettingsDataSource.save(
                    if (language == Language.FRENCH) {
                        AppSettingsKeys.LAST_CALL_DATA_FETCH_DATETIME_FR
                    } else {
                        AppSettingsKeys.LAST_CALL_DATA_FETCH_DATETIME_EN
                    },
                    "${Clock.System.now().toEpochMilliseconds()}"
                )
                val languageResponse = getAllDataQueryFromDate(language).execute()
                manageResponse(languageResponse, language)?.let { error ->
                    onError = error
                }
            } else {
                SettingsDataSource.save(
                    AppSettingsKeys.LAST_CALL_DATA_FETCH_DATETIME_FR,
                    "${Clock.System.now().toEpochMilliseconds()}"
                )
                val frenchResponse = getAllDataQueryFromDate(Language.FRENCH).execute()
                manageResponse(frenchResponse, Language.FRENCH)?.let { error ->
                    onError = error
                } ?: run {
                    SettingsDataSource.save(
                        AppSettingsKeys.LAST_CALL_DATA_FETCH_DATETIME_EN,
                        "${Clock.System.now().toEpochMilliseconds()}"
                    )
                    val englishResponse = getAllDataQueryFromDate(Language.ENGLISH).execute()
                    manageResponse(englishResponse, Language.ENGLISH)?.let { error ->
                        onError = error
                    }
                }
            }
        } catch (ex: Exception) {
            onError = DatoCMSAPIError.NetworkError(ex)
        }
        onError?.let { error ->
            language?.let {
                if (!Database.data.hasContentInDB(it)) {
                    throw error
                }
            } ?: run {
                Language.entries.forEach { lang ->
                    if (!Database.data.hasContentInDB(lang)) {
                        throw error
                    }
                }
            }
        } ?: run {
            language?.let {
                if (!Database.data.hasContentInDB(it)) {
                    throw DatoCMSAPIError.ApiError(Exception("No data in DB"))
                }
            } ?: run {
                Language.entries.forEach { lang ->
                    if (!Database.data.hasContentInDB(lang)) {
                        throw DatoCMSAPIError.ApiError(Exception("No data in DB"))
                    }
                }
            }
        }
    }

    private suspend fun manageResponse(
        response: ApolloResponse<AllDataFromDateQuery.Data>,
        lang: Language
    ): DatoCMSAPIError? {
        response.errors?.let { errors ->
            val ex =
                Exception(
                    errors.joinToString {
                        it.message
                    }
                )
            AppLogger.e("fetchAllData API ERROR", ex)
            return DatoCMSAPIError.ApiError(source = ex)
        } ?: run {
            var isSavedOk = false
            response.data?.allCategories?.let { data ->
                isSavedOk = Database.data.saveQuestions(data, lang.code)
            }
            if (isSavedOk) {
                SettingsDataSource.save(
                    if (lang == Language.FRENCH) {
                        AppSettingsKeys.LAST_DATA_UPDATE_DATETIME_FR
                    } else {
                        AppSettingsKeys.LAST_DATA_UPDATE_DATETIME_EN
                    },
                    "${Clock.System.now().toEpochMilliseconds()}"
                )
            } else {
                return@run DatoCMSAPIError.ApiError(Exception("Bad Data"))
            }
        }
        return null
    }
}
