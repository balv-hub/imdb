package com.balv.imdb.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavigationBar(
    currentTab: BottomNavigationItem,
    onClick: (BottomNavigationItem) -> Unit
) {
    androidx.compose.material3.NavigationBar {
        BottomNavigationItem.entries.forEach { entry ->
            NavigationBarItem(
                selected = entry == currentTab,
                onClick = { onClick(entry) },
                icon = {
                   Icon(imageVector = entry.icon, contentDescription = "bottom nav icon ${entry.name}")
                       },
                label = { Text(entry.name) }
            )
        }
    }
}

enum class BottomNavigationItem(
    val icon: ImageVector,
    val destination: String
) {
    Home(Icons.Default.Home, "home"),
    Search(Icons.Default.Search, "search"),
    Favorite(Icons.Default.Favorite, "favorite")
}