package com.cacd2.cacdgame.android.features

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.isSearchCriteriaOpen
import com.cacd2.cacdgame.android.showAppBarContent
import com.cacd2.cacdgame.android.showFilterHistoryButton
import com.cacd2.cacdgame.android.showHideLexicalButton
import com.cacd2.cacdgame.android.showSearchLexicalButton
import com.cacd2.cacdgame.android.username
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Created by francois.dabonot@cacd2.fr on 11/05/2023.
 */

enum class Router(private val route: String) {
    History("home"),
    OnBoarding("onboarding"),
    GameSelection("gameselection"),
    Contact("contact"),
    ContactFromResult("contactfromresult"),
    Lexical("lexical"),
    LexicalDetail("lexical/{questionId}"),
    Setting("settings"),
    GameDifficulty("gamedifficulty/{gameId}"),
    GamePlay("gameplay/{gamedifficulty}/{gameId}"),
    AnswerDefinition("answerdefinition/{questionId}"),
    AnswerDefinitionFromGamePlay("answerdefinitionfromgameplay/{questionId}"),
    GameResult("gameresult/{historyId}"),
    GameResultFromHistory("gameresultfromHistory/{historyId}"),
    HistoryAnswers("historyanswsers/{historyId}/{isSuccess}"),
    Accessibility("accessibility")
    ;

    companion object : KoinComponent {
        private val appContext: AppContext by inject()

        fun startDestination(): Router {
            return runBlocking(Dispatchers.Default) {
                return@runBlocking if (!appContext.username.value.isNullOrEmpty()) {
                    GameSelection
                } else {
                    OnBoarding
                }
            }
        }

        val bottomBarItems: List<Router>
            get() =
                listOf(
                    Lexical,
                    History,
                    GameSelection,
                    Contact,
                    Setting
                )

        fun updateAppBarFor(route: Router) {
            appContext.showAppBarContent.value = false
            appContext.showHideLexicalButton.value = false
            appContext.showSearchLexicalButton.value = false
            appContext.showFilterHistoryButton.value = false
            when (route) {
                History -> {
                    appContext.showFilterHistoryButton.value = true
                }
                OnBoarding -> {
                    appContext.showAppBarContent.value = true
                }
                Lexical -> {
                    if (appContext.isSearchCriteriaOpen.value) {
                        appContext.showHideLexicalButton.value = true
                    } else {
                        appContext.showSearchLexicalButton.value = true
                    }
                }

                else -> {}
            }
        }

        fun isAppBarVisibleFor(route: String?): Boolean {
            val routes =
                arrayOf(
                    Lexical.route
                )
            return !routes.contains(route)
        }

        fun routeHasBackButton(route: String?): Boolean {
            val routes =
                arrayOf(
                    ContactFromResult.route,
                    HistoryAnswers.route,
                    LexicalDetail.route,
                    AnswerDefinition.route,
                    GameDifficulty.route,
                    GameResultFromHistory.route,
                    Accessibility.route
                )
            return routes.contains(route)
        }

        fun routeHasBottomBar(route: String?): Boolean {
            val routes =
                arrayOf(
                    GameSelection.route,
                    Contact.route,
                    Setting.route,
                    GameResult.route,
                    Lexical.route,
                    History.route
                )
            return routes.contains(route)
        }
    }

    val args: List<NamedNavArgument>
        get() =
            when (this) {
                AnswerDefinition ->
                    listOf(
                        navArgument("questionId") {
                            type = NavType.IntType
                        }
                    )

                GamePlay ->
                    listOf(
                        navArgument("gameId") {
                            type = NavType.IntType
                        }
                    )

                else -> emptyList()
            }

    fun route(vararg args: Any? = emptyArray()): String {
        return when (this) {
            OnBoarding -> route
            GameSelection -> route
            GameDifficulty -> {
                route.apply {
                    args.takeIf { it.isNotEmpty() }?.let { params ->
                        return replace("{gameId}", "${params[0]}")
                    }
                }
            }

            GamePlay -> {
                route.apply {
                    args.takeIf { it.isNotEmpty() }?.let { params ->
                        return replace("{gamedifficulty}", "${params[0]}")
                            .replace("{gameId}", "${params[1]}")
                    }
                }
            }

            GameResult, GameResultFromHistory -> {
                route.apply {
                    args.takeIf { it.isNotEmpty() }?.let { params ->
                        return replace("{historyId}", "${params[0]}")
                    }
                }
            }

            AnswerDefinition, AnswerDefinitionFromGamePlay -> {
                route.apply {
                    args.takeIf { it.isNotEmpty() }?.let { params ->
                        return replace("{questionId}", "${params[0]}")
                    }
                }
            }

            HistoryAnswers -> {
                route.apply {
                    args.takeIf { it.isNotEmpty() }?.let { params ->
                        return replace("{historyId}", "${params[0]}")
                            .replace("{isSuccess}", "${params[1]}")
                    }
                }
            }

            LexicalDetail -> {
                route.apply {
                    args.takeIf { it.isNotEmpty() }?.let { params ->
                        return replace("{questionId}", "${params[0]}")
                    }
                }
            }

            else -> route
        }
    }

    val tabBarTitle: Int
        @StringRes
        get() =
            when (this) {
                History -> R.string.history_title
                GameSelection -> R.string.tab_quiz
                Contact -> R.string.tab_contact
                Lexical -> R.string.tab_lexique
                Setting -> R.string.tab_parametre
                else -> 0
            }

    val tabBarIcon: Int
        @DrawableRes
        get() =
            when (this) {
                History -> R.drawable.history
                GameSelection -> R.drawable.tabbar_quiz
                Contact -> R.drawable.tabbar_contact
                Lexical -> R.drawable.tabbar_lexique
                Setting -> R.drawable.tabbar_settings
                else -> 0
            }
}
