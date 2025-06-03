package com.balv.imdb.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Root(
    navigate: (String) -> Unit
) {

    val tabList = listOf(
        BottomNavigationItem.Home,
        BottomNavigationItem.Search,
        BottomNavigationItem.Favorite
    )

    var currentTab by remember { mutableStateOf(BottomNavigationItem.Home) }
    var previousTab by remember { mutableStateOf(currentTab) }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(title = { Text("The Movie DB") })
        },
        bottomBar = {
            BottomNavigationBar(currentTab) { tab ->
                previousTab = currentTab
                currentTab = tab
            }
        }
    ) { paddingValues ->
        val currentIndex = tabList.indexOf(currentTab)
        val previousIndex = tabList.indexOf(previousTab)

        val direction = if (currentIndex > previousIndex) 1 else -1

        AnimatedContent(
            targetState = currentTab,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { fullWidth -> fullWidth * direction }
                ) togetherWith  slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { fullWidth -> -fullWidth * direction }
                )
            },
            modifier = Modifier.padding(paddingValues)
        ) { targetTab ->
            when (targetTab) {
                BottomNavigationItem.Home -> HomeScreen(paddingValues = PaddingValues(0.dp)) {
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

