package com.cacd2.cacdgame.android.features

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.AppEvent
import com.cacd2.cacdgame.EventBus
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.appbarTitle
import com.cacd2.cacdgame.android.consentManagerDisplayed
import com.cacd2.cacdgame.android.currentLanguageCode
import com.cacd2.cacdgame.android.defaultStatusBarColor
import com.cacd2.cacdgame.android.emptyStatusBarColor
import com.cacd2.cacdgame.android.features.Router.Companion.updateAppBarFor
import com.cacd2.cacdgame.android.features.answerdefinition.AnswerDefinitionScreen
import com.cacd2.cacdgame.android.features.consent.ConsentManagerScreen
import com.cacd2.cacdgame.android.features.contact.ContactScreen
import com.cacd2.cacdgame.android.features.gamedifficulty.GameDifficultyScreen
import com.cacd2.cacdgame.android.features.gameplay.GamePlayScreen
import com.cacd2.cacdgame.android.features.gameresult.GameResultScreen
import com.cacd2.cacdgame.android.features.gameselection.GameSelectionScreen
import com.cacd2.cacdgame.android.features.history.HomeScreen
import com.cacd2.cacdgame.android.features.lexique.LexicalScreen
import com.cacd2.cacdgame.android.features.lexique.lexiquedetail.LexicalDetailActionScreen
import com.cacd2.cacdgame.android.features.lexique.lexiquedetail.LexicalDetailScreen
import com.cacd2.cacdgame.android.features.onboarding.OnBoardingScreen
import com.cacd2.cacdgame.android.features.questionlisting.QuestionListingScreen
import com.cacd2.cacdgame.android.features.settings.SettingsScreen
import com.cacd2.cacdgame.android.features.settings.accessibility.AccessibilityScreen
import com.cacd2.cacdgame.android.features.ui.AppBarView
import com.cacd2.cacdgame.android.features.ui.BottomBarView
import com.cacd2.cacdgame.android.features.ui.LoadQuestionsDataView
import com.cacd2.cacdgame.android.forceHideBottomBar
import com.cacd2.cacdgame.android.forceHideFabButton
import com.cacd2.cacdgame.android.forcedStatusBarColor
import com.cacd2.cacdgame.android.gameId
import com.cacd2.cacdgame.android.model.label
import com.cacd2.cacdgame.android.model.title
import com.cacd2.cacdgame.android.save
import com.cacd2.cacdgame.android.screenMode
import com.cacd2.cacdgame.android.username
import com.cacd2.cacdgame.android.utility.switchTabs
import com.cacd2.cacdgame.datasource.api.game.DatoCMSAPI
import com.cacd2.cacdgame.datasource.api.game.UpdateMode
import com.cacd2.cacdgame.datasource.database.Database
import com.cacd2.cacdgame.datasource.database.dao.saveHistory
import com.cacd2.cacdgame.model.Difficulty
import com.cacd2.cacdgame.model.GameChoice
import com.cacd2.cacdgame.tools.logger.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject

/**
 * Created by francois.dabonot@cacd2.fr on 10/05/2023.
 */
@Composable
fun MainApp(appContext: AppContext = koinInject()) {
    var initialDataLoaded by rememberSaveable { mutableStateOf(false) }
    var updateMode by rememberSaveable { mutableStateOf<UpdateMode?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isFetching by remember { mutableStateOf(false) }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cacd2.fr/mobile-privacy.html"))
    val defaultStatusBarColor = MaterialTheme.defaultStatusBarColor

    suspend fun doSilentUpdate() {
        if (updateMode == UpdateMode.SILENT && !isFetching) {
            AppLogger.i("Doing silent update on Launch")
            coroutineScope.launch {
                try {
                    isFetching = true
                    DatoCMSAPI.fetchAllData(appContext.currentLanguageCode.value)
                } catch (error: Exception) {
                    AppLogger.e("SILENT UPDATE FAILED", throwable = error)
                } finally {
                    isFetching = false
                }
            }
        }
    }

    suspend fun checkUpdate() {
        if (updateMode == null && appContext.consentManagerDisplayed.value) {
            updateMode = DatoCMSAPI.updateModeFor(appContext.currentLanguageCode.value)
            AppLogger.i("updateMode = $updateMode")
        }
    }

    LaunchedEffect(appContext.consentManagerDisplayed.value) {
        if (appContext.consentManagerDisplayed.value && updateMode == null) {
            checkUpdate()
        }
    }

    LaunchedEffect(updateMode) {
        AppLogger.i("Check silent update on Launch")
        doSilentUpdate()
    }

    Box {
        if (!appContext.consentManagerDisplayed.value) {
            AppLogger.i("Display consent manager")
            MainContainer()
            trackScreenView(name = "ConsentManagerFromAppStart")
            ConsentManagerScreen(
                modifier = Modifier.fillMaxSize(),
                onValidation = {
                    AppLogger.i("onValidation consent manager")
                    coroutineScope.launch {
                        save()
                    }
                    appContext.consentManagerDisplayed.value = true
                }
            ) {
                context.startActivity(intent)
            }
        } else if (updateMode == UpdateMode.FULL && !initialDataLoaded) {
            AppLogger.i("UpdateMode = $updateMode && $initialDataLoaded == false")
            Surface(modifier = Modifier.fillMaxSize()) {
                appContext.forcedStatusBarColor.value = MaterialTheme.emptyStatusBarColor.toArgb()
                LoadQuestionsDataView(language = appContext.currentLanguageCode.value) {
                    updateMode = null
                    initialDataLoaded = true
                }
            }
        } else {
            Box {
                MainContainer()
                if (updateMode == UpdateMode.FULL) {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        appContext.forcedStatusBarColor.value =
                            MaterialTheme.emptyStatusBarColor.toArgb()
                        LoadQuestionsDataView(language = appContext.currentLanguageCode.value) {
                            updateMode = null
                            appContext.forcedStatusBarColor.value = defaultStatusBarColor.toArgb()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContainer(appContext: AppContext = koinInject()) {
    val navController = rememberNavController()

    AppScaffold(
        navController = navController,
        appContext = appContext
    ) {
        AppNavigationHost(
            modifier = Modifier.padding(it),
            navController = navController,
            appContext = appContext
        )
    }
}

@Composable
fun AppScaffold(
    appContext: AppContext = koinInject(),
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            AppBarView(
                appContext = appContext,
                canShowBackButton =
                Router.routeHasBackButton(
                    currentBackStackEntry?.destination?.route
                ),
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        },
        bottomBar = {
            BottomBarView(
                navController = navController,
                appContext = appContext,
                canShowView =
                Router.routeHasBottomBar(
                    currentBackStackEntry?.destination?.route
                )
            )
        },
        floatingActionButton = {
            FabCompose(
                appContext,
                navController,
                canShowFabButton =
                Router.routeHasBottomBar(
                    currentBackStackEntry?.destination?.route
                )
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true
    ) {
        content(it)
    }
}

@Composable
fun FabCompose(
    appContext: AppContext,
    navController: NavHostController,
    canShowFabButton: Boolean
) {
    if (canShowFabButton && !appContext.forceHideFabButton.value) {
        FloatingActionButton(
            modifier = Modifier,
            onClick = {
                navController.switchTabs(Router.GameSelection.route())
            }
        ) {
            Icon(painter = painterResource(id = R.drawable.tabbar_quiz), contentDescription = null)
        }
    }
}

@Composable
fun AppNavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appContext: AppContext = koinInject()
) {
    val coroutine = rememberCoroutineScope()
    val emptyStatusBarColor = MaterialTheme.emptyStatusBarColor
    val defaultStatusBarColor = MaterialTheme.defaultStatusBarColor

    LaunchedEffect(navController.currentBackStackEntry) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (currentRoute == Router.OnBoarding.route()) {
            appContext.forcedStatusBarColor.value = emptyStatusBarColor.toArgb()
        } else {
            appContext.forcedStatusBarColor.value = defaultStatusBarColor.toArgb()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Router.startDestination().route(),
        modifier = modifier
    ) {
        composable(
            route = Router.History.route()
        ) {
            trackScreenView("History")
            updateAppBarFor(Router.History)
            appContext.appbarTitle.value = stringResource(id = R.string.history_title)
            HomeScreen(onSelectionHistory = {
                trackEvent("HistoryAnswers From History")
                navController.navigate(Router.GameResultFromHistory.route(it.id))
            }, onGoToNewGame = {
                trackEvent("GameSelection from History")
                navController.switchTabs(Router.GameSelection.route())
            })
        }
        composable(
            route = Router.OnBoarding.route()
        ) {
            trackScreenView("OnBoarding")
            updateAppBarFor(Router.OnBoarding)
            appContext.appbarTitle.value = null
            OnBoardingScreen {
                coroutine.launch {
                    appContext.username.value = it
                    save()
                }
                navController.navigate(Router.GameSelection.route()) {
                    popUpTo(route = Router.OnBoarding.route()) {
                        inclusive = true
                    }
                }
            }
        }
        composable(
            route = Router.GameSelection.route()
        ) {
            trackScreenView("GameSelection")
            updateAppBarFor(Router.GameSelection)
            appContext.gameId.value = null
            appContext.appbarTitle.value = stringResource(id = R.string.quiz_selection_title)
            GameSelectionScreen(
                onGoBack = {
                    navController.popBackStack()
                },
                onGoToGamePlay = {
                    appContext.gameId.value = it.gameId
                    navController.navigate(Router.GameDifficulty.route(it.gameId))
                }
            )
        }
        composable(
            route = Router.GameDifficulty.route()
        ) {
            updateAppBarFor(Router.GameDifficulty)
            appContext.appbarTitle.value = stringResource(id = R.string.title_difficulty)
            val gameId = it.arguments!!.getString("gameId")!!
            val gameChoice = GameChoice.fromId(gameId)
            trackScreenView(
                "GameDifficulty",
                mapOf(
                    "game" to stringResource(id = gameChoice.label)
                )
            )
            GameDifficultyScreen(
                game = gameChoice,
                onGoBack = {
                    navController.popBackStack()
                },
                onSelectionDifficulty = { cGameId, difficultyId ->
                    navController.navigate(Router.GamePlay.route(difficultyId.id, cGameId.gameId))
                }
            )
        }
        composable(
            route = Router.GamePlay.route()
        ) {
            updateAppBarFor(Router.GamePlay)
            val gameId = it.arguments!!.getString("gameId")!!
            val gameDifficulty = it.arguments!!.getString("gamedifficulty")!!
            val gameChoice = GameChoice.fromId(gameId)
            val difficultyChoice = Difficulty.fromId(gameDifficulty)
            appContext.appbarTitle.value = stringResource(id = gameChoice.title)
            trackScreenView(
                "GamePlay",
                mapOf(
                    "game" to stringResource(id = gameChoice.label),
                    "gamedifficulty" to stringResource(id = difficultyChoice.label)
                )
            )
            GamePlayScreen(
                gameChoice = gameChoice,
                onShowResult = { questionsAnswered ->
                    coroutine.launch {
                        withContext(Dispatchers.Default) {
                            val historyId =
                                Database.data.saveHistory(
                                    category = gameChoice,
                                    questions = questionsAnswered
                                )
                            withContext(Dispatchers.Main) {
                                if (historyId == -1L) {
                                    navController.popBackStack()
                                } else {
                                    navController.navigate(Router.GameResult.route(historyId))
                                }
                            }
                        }
                    }
                },
                gameDifficulty = difficultyChoice,
                onCancelGame = {
                    navController.popBackStack()
                },
                onShowDefinition = { question ->
                    navController.navigate(Router.AnswerDefinitionFromGamePlay.route(question.id))
                }
            )
        }
        composable(
            route = Router.AnswerDefinition.route()
        ) {
            updateAppBarFor(Router.AnswerDefinition)
            appContext.gameId.value = null
            appContext.appbarTitle.value = stringResource(id = R.string.title_definition)
            val questionId = it.arguments!!.getString("questionId")!!
            trackScreenView("AnswerDefinition", mapOf("questionId" to questionId.toString()))
            AnswerDefinitionScreen(questionId, nextQuestion = null)
        }
        composable(
            route = Router.AnswerDefinitionFromGamePlay.route()
        ) {
            updateAppBarFor(Router.AnswerDefinitionFromGamePlay)
            appContext.gameId.value = null
            appContext.appbarTitle.value = stringResource(id = R.string.title_definition)
            val questionId = it.arguments!!.getString("questionId")!!
            trackScreenView(
                "AnswerDefinitionFromGamePlay",
                mapOf("questionId" to questionId.toString())
            )
            AnswerDefinitionScreen(questionId) {
                coroutine.launch {
                    EventBus.publish(AppEvent.NEXT_QUESTION)
                    navController.popBackStack()
                }
            }
        }
        composable(
            route = Router.GameResult.route()
        ) {
            trackScreenView("GameResult")
            updateAppBarFor(Router.GameResult)
            appContext.appbarTitle.value = stringResource(id = R.string.result_title)
            val historyId = it.arguments!!.getString("historyId")!!.toLong()
            appContext.forceHideBottomBar.value = true
            GameResultScreen(
                shouldDisplayLoaderView = true,
                shouldDisplayResultBanner = true,
                historyId = historyId,
                onClose = {
                    navController.popBackStack(
                        route = Router.GameSelection.route(),
                        inclusive = false,
                        saveState = false
                    )
                },
                onShowAnswersHistory = { showSuccess ->
                    navController.navigate(Router.HistoryAnswers.route(historyId, showSuccess))
                },
                onShowContact = {
                    navController.navigate(Router.ContactFromResult.route())
                }
            )
        }
        composable(
            route = Router.GameResultFromHistory.route()
        ) {
            trackScreenView("GameResultFromHistory")
            updateAppBarFor(Router.GameResultFromHistory)
            appContext.appbarTitle.value = stringResource(id = R.string.result_title)
            val historyId = it.arguments!!.getString("historyId")!!.toLong()
            GameResultScreen(
                shouldDisplayLoaderView = false,
                shouldDisplayResultBanner = false,
                historyId = historyId,
                onClose = {
                    navController.popBackStack()
                },
                onShowAnswersHistory = { showSuccess ->
                    navController.navigate(Router.HistoryAnswers.route(historyId, showSuccess))
                },
                onShowContact = {
                    navController.navigate(Router.ContactFromResult.route())
                }
            )
        }
        composable(
            route = Router.Setting.route()
        ) {
            appContext.appbarTitle.value = stringResource(id = R.string.title_parameter)
            updateAppBarFor(Router.Setting)
            trackScreenView("Setting")
            SettingsScreen(
                onSelectAccessibility = {
                    navController.navigate(Router.Accessibility.route())
                },
                showOnboarding = {
                    navController.popBackStack()
                    navController.navigate(
                        route = Router.OnBoarding.route()
                    ) {
                        this.launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Router.Contact.route()
        ) {
            trackScreenView("ContactFromTab")
            updateAppBarFor(Router.Contact)
            appContext.appbarTitle.value = stringResource(id = R.string.title_contacter_cacd2)
            ContactScreen()
        }
        composable(
            route = Router.ContactFromResult.route()
        ) {
            trackScreenView("ContactFromResult")
            updateAppBarFor(Router.ContactFromResult)
            appContext.appbarTitle.value = stringResource(id = R.string.title_contacter_cacd2)
            ContactScreen(shouldShowBottomBar = false)
        }
        composable(
            route = Router.Lexical.route()
        ) {
            trackScreenView("LexicalQuestionListing")
            updateAppBarFor(Router.Lexical)
            appContext.appbarTitle.value = stringResource(id = R.string.tab_lexique)
            LexicalScreen(onSelectQuestionId = {
                navController.navigate(Router.AnswerDefinition.route(it))
            })
        }
        composable(
            route = Router.LexicalDetail.route()
        ) {
            updateAppBarFor(Router.LexicalDetail)
            appContext.appbarTitle.value = stringResource(id = R.string.tab_lexique)
            val questionId = it.arguments!!.getString("questionId")!!
            trackScreenView("LexicalDetail", mapOf("questionId" to questionId.toString()))
            LexicalDetailScreen(questionId = questionId) { action ->
                when (action.first) {
                    LexicalDetailActionScreen.OPEN_DEF ->
                        navController.navigate(
                            Router.AnswerDefinition.route(action.second)
                        )

                    else -> {}
                }
            }
        }
        composable(
            route = Router.HistoryAnswers.route()
        ) {
            updateAppBarFor(Router.HistoryAnswers)
            val historyId = it.arguments!!.getString("historyId")!!.toLong()
            val isSuccess = it.arguments?.getString("isSuccess")?.toBooleanStrictOrNull()
            trackScreenView(
                "HistoryQuestionListing",
                mapOf(
                    "type" to
                        isSuccess?.let {
                            if (isSuccess) {
                                "ONLY_CORRECT"
                            } else {
                                "ONLY_ERROR"
                            }
                        }.run { "ALL" }
                )
            )
            val idRes =
                when (isSuccess) {
                    true -> R.string.title_good_answsers
                    false -> R.string.title_bad_answsers
                    else -> R.string.title_my_answsers
                }
            appContext.appbarTitle.value = stringResource(id = idRes)
            QuestionListingScreen(
                historyId = historyId,
                isSuccess = isSuccess,
                onSelectQuestionId = { questionId ->
                    navController.navigate(Router.AnswerDefinition.route(questionId))
                }
            )
        }
        composable(
            route = Router.Accessibility.route()
        ) {
            trackScreenView("Accessibility")
            updateAppBarFor(Router.Accessibility)
            appContext.appbarTitle.value = stringResource(id = R.string.accessibility_label)
            AccessibilityScreen(onUpdateScreenMode = {
                appContext.screenMode.value = it
                coroutine.launch {
                    save()
                }
            })
        }
    }
}

@Preview(device = "id:Nexus One", showBackground = false, showSystemUi = false)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text(text = "CONTENT")
        }
    }
}
