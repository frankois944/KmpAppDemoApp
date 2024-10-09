package com.cacd2.cacdgame.android.features.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cacd2.cacdgame.AppContext
import com.cacd2.cacdgame.android.AppTheme
import com.cacd2.cacdgame.android.R
import com.cacd2.cacdgame.android.features.Router
import com.cacd2.cacdgame.android.forceHideBottomBar
import org.koin.compose.koinInject

/**
 * Created by frankois on 12/05/2023.
 */
@Composable
fun BottomBarView(
    navController: NavHostController,
    canShowView: Boolean = false,
    appContext: AppContext = koinInject()
) {
    val isDisplayed = canShowView && !appContext.forceHideBottomBar.value

    if (isDisplayed) {
        // https://developer.android.com/jetpack/compose/navigation?hl=fr#bottom-nav
        BottomAppBar(cutoutShape = CircleShape) {
            navController.let {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                Router.bottomBarItems.forEach { screen ->
                    if (screen.tabBarTitle == R.string.tab_quiz) {
                        Spacer(Modifier.weight(1f, true))
                        /*BottomNavigationItem(
                            selected = false,
                            icon = {},
                            onClick = {},
                            modifier = Modifier.alpha(0f)
                        )*/
                    } else {
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painterResource(id = screen.tabBarIcon),
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(screen.tabBarTitle),
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            },
                            selected =
                            currentDestination?.hierarchy?.any {
                                it.route == screen.route()
                            } == true,
                            onClick = {
                                if (!Router.bottomBarItems.map {
                                        it.route()
                                    }
                                        .contains(
                                            navController.currentBackStackEntry?.destination?.route
                                        )
                                ) {
                                    while (navController.currentBackStack.value.size > 2) {
                                        navController.popBackStack()
                                    }
                                }
                                navController.navigate(screen.route()) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:Nexus One")
@Composable
fun BottomBarPreview() {
    val navController = rememberNavController()
    AppTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            BottomBarView(
                navController,
                appContext = AppContext(),
                canShowView = true
            )
        }
    }
}
