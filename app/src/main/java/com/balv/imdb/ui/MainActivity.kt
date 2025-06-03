package com.balv.imdb.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.balv.imdb.ui.detail.MovieDetailScreen
import com.balv.imdb.ui.favorite.FavoriteScreen
import com.balv.imdb.ui.home.BottomNavigationBar
import com.balv.imdb.ui.home.BottomNavigationItem
import com.balv.imdb.ui.home.HomeScreen
import com.balv.imdb.ui.search.SearchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                MyAppNavHost()
            }
        }
    }
}

@Composable
fun MyAppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "root"
    ) {
        composable(
            "root",
            enterTransition = NavAnimations.homeEnter,
            exitTransition = NavAnimations.slideOutToLeft,
            popEnterTransition = NavAnimations.slideInFromLeft,
            popExitTransition = NavAnimations.slideOutToRight
        ) {
            Root {
                navController.navigate(it)
            }
        }

        composable(
            route = "detail/{movieId}",
            enterTransition = NavAnimations.slideInFromRight,
            exitTransition = NavAnimations.slideOutToLeft,
            popEnterTransition = NavAnimations.slideInFromRight,
            popExitTransition = NavAnimations.slideOutToRight,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { _ ->
            MovieDetailScreen {
                navController.popBackStack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Root(
    navigate: (String) -> Unit
) {
    var currentTab by rememberSaveable {
        mutableStateOf(BottomNavigationItem.Home)
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(title = { Text("The Movie DB") })
        },
        bottomBar = {
            BottomNavigationBar(
                currentTab
            ) { tab ->
                currentTab = tab
            }
        }
    ) { paddingValues ->
        when (currentTab) {
            BottomNavigationItem.Home -> HomeScreen(paddingValues = paddingValues) {
                navigate("detail/$it")
            }
            BottomNavigationItem.Search -> SearchScreen {
                currentTab = BottomNavigationItem.Home
            }
            BottomNavigationItem.Favorite -> FavoriteScreen {
                currentTab = BottomNavigationItem.Home
            }
        }
    }
}

object NavAnimations {
    val slideInFromRight: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it }, // Full width from right
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        ) + fadeIn(animationSpec = tween(DEFAULT_ANIMATION_DURATION))
    }

    val slideOutToLeft: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -it / 2 }, // Slide out to left (partially)
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        ) + fadeOut(animationSpec = tween(DEFAULT_ANIMATION_DURATION))
    }

    val slideOutToRight: AnimatedContentTransitionScope<*>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it }, // Full width to right
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        ) + fadeOut(animationSpec = tween(DEFAULT_ANIMATION_DURATION))
    }

    val slideInFromLeft: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it / 2 }, // Slide in from left (partially)
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        ) + fadeIn(animationSpec = tween(DEFAULT_ANIMATION_DURATION))
    }

    val homeEnter: AnimatedContentTransitionScope<*>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it / 2 },
            animationSpec = tween(DEFAULT_ANIMATION_DURATION)
        ) + fadeIn(animationSpec = tween(DEFAULT_ANIMATION_DURATION))
    }

}
private const val DEFAULT_ANIMATION_DURATION = 400

