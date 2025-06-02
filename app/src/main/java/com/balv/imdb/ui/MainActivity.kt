package com.balv.imdb.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
        composable("root") {
            Root {
                navController.navigate(it)
            }
        }

        composable(
            route = "detail/{movieId}",
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
            BottomNavigationItem.Search -> SearchScreen()
            BottomNavigationItem.Profile -> HomeScreen(paddingValues = paddingValues) { }
        }
    }
}