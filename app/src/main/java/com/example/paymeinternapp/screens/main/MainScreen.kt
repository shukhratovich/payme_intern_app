package com.example.paymeinternapp.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.example.paymeinternapp.R
import com.example.paymeinternapp.screens.news.categories.NewsScreen
import com.example.paymeinternapp.screens.stopwatcher.StopwatchScreen
import com.example.paymeinternapp.screens.weather.WeatherScreen
import kotlinx.coroutines.launch

class MainScreen : Screen {
    @Composable
    override fun Content() {
        MainContent()
    }
}


data class NavigationItem(
    val title: String, val icon: Int, val screenTitle: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(modifier: Modifier = Modifier) {
    val items = listOf(
        NavigationItem(
            title = "Stopwatch",
            icon = R.drawable.ic_timer,
            screenTitle = "Stopwatch"
        ),
        NavigationItem(
            title = "Weather",
            icon = R.drawable.ic_weather,
            screenTitle = "Weather"
        ),
        NavigationItem(
            title = "News",
            icon = R.drawable.ic_news,
            screenTitle = "News"
        )
    )
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "back",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(12.dp)
                                .clickable { scope.launch { drawerState.close() } }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            label = {
                                Text(text = item.title)
                            }, selected = index == selectedItemIndex, onClick = {
                                scope.launch {
                                    selectedItemIndex = index
                                    drawerState.close()
                                }
                            }, icon = {
                                Image(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(item.icon),
                                    contentDescription = item.title
                                )
                            }, modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                    }
                }
            }, drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarColors(
                            containerColor = Color.White,
                            scrolledContainerColor = Color.Transparent,
                            navigationIconContentColor = Color.Black,
                            titleContentColor = Color.Black,
                            actionIconContentColor = Color.Transparent
                        ), title = {
                            Text(text = items[selectedItemIndex].screenTitle)
                        }, navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        })
                }) { paddingValues ->

                when (selectedItemIndex) {
                    0 -> StopwatchScreen(modifier.padding(paddingValues)).Content()
                    1 -> WeatherScreen(modifier.padding(paddingValues)).Content()
                    2 -> NewsScreen(modifier.padding(paddingValues)).Content()
                }
            }
        }
    }
}