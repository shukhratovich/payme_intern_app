package com.example.paymeinternapp.screens.news.categories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.domain.model.ui.CategoryNews
import com.example.paymeinternapp.R
import com.example.paymeinternapp.screens.news.details.NewsDetails
import com.example.paymeinternapp.utils.CategoryChip
import com.example.paymeinternapp.utils.NewsCard
import com.example.paymeinternapp.utils.toIntOffset
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class NewsScreen(private val modifier: Modifier = Modifier) : Screen {
    @Composable
    override fun Content() {
        val viewModel: NewsContract.ViewModel = getViewModel<NewsViewModel>()
        val uiState = viewModel.uiState.collectAsState()
        NewsScreenContent(modifier, uiState.value, viewModel::onEventDispatcher)
    }
}

@Composable
private fun NewsScreenContent(
    modifier: Modifier = Modifier,
    uiState: NewsContract.UiState,
    onEventDispatcher: (NewsContract.Intent) -> Unit
) {
    val navigator = LocalNavigator.currentOrThrow
    val searchText = remember { mutableStateOf("") }
    var selectedCategory = remember { mutableStateOf(CategoryNews.BUSINESS) }
    var selectedSource = remember { mutableStateOf("") }
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isRefreshSwiped)
    var popUpMenuClicked by remember { mutableStateOf(false) }
    var popUpMenuPosition by remember { mutableStateOf(IntOffset.Zero) }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                onEventDispatcher(NewsContract.Intent.PulledForRefresh)
            },
            modifier = Modifier.padding(innerPadding),
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = swipeRefreshState,
                    refreshTriggerDistance = 140.dp,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    refreshingOffset = 124.dp
                )
            },
        ) {
            Box(
                modifier = Modifier
                    .nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                ) {
                    item {
                        Column {
                            OutlinedTextField(
                                value = searchText.value,
                                onValueChange = {
                                    searchText.value = it
                                    onEventDispatcher(NewsContract.Intent.SearchByQuery(it))
                                },
                                placeholder = {
                                    Text(
                                        "Search file...",
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = if (uiState.isFilterByCategory) "Filtered by category" else "Filtered by source",
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(horizontal = 24.dp)
                                )
                                IconButton(
                                    onClick = { popUpMenuClicked = true },
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(horizontal = 24.dp)
                                        .size(24.dp)
                                        .onGloballyPositioned { coordinates ->
                                            popUpMenuPosition =
                                                coordinates.positionInWindow().toIntOffset()
                                        },
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_filter),
                                        contentDescription = "Filter settings",
                                    )
                                }
                                DropdownMenu(
                                    expanded = popUpMenuClicked,
                                    onDismissRequest = { popUpMenuClicked = false },
                                    offset = with(LocalDensity.current) {
                                        DpOffset(
                                            x = popUpMenuPosition.x.toDp(),
                                            y = (popUpMenuPosition.y - 400.dp.toPx()).toDp()
                                        )
                                    }
                                ) {
                                    DropdownMenuItem(
                                        onClick = {
                                            onEventDispatcher(NewsContract.Intent.ByCategoryClicked)
                                            popUpMenuClicked = false
                                        },
                                        text = { Text("by Category") }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            onEventDispatcher(NewsContract.Intent.BySourcesClicked)
                                            popUpMenuClicked = false
                                        },
                                        text = { Text("by Sources") }
                                    )
                                    HorizontalDivider()
                                    DropdownMenuItem(
                                        onClick = {
                                            popUpMenuClicked = false
                                        },
                                        text = { Text("Cancel") }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            if (uiState.isFilterByCategory) {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(uiState.categories) { item ->
                                        CategoryChip(
                                            text = item.name,
                                            isSelected = item == selectedCategory.value,
                                            onClick = {
                                                selectedCategory.value = item
                                                onEventDispatcher(
                                                    NewsContract.Intent.ClickedCategory(
                                                        item
                                                    )
                                                )
                                            },
                                            colorBoxSelected = MaterialTheme.colorScheme.surfaceTint,
                                            colorBoxUnSelected = MaterialTheme.colorScheme.surfaceDim,
                                            colorText = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    items(uiState.sources) { item ->
                                        CategoryChip(
                                            text = item.name,
                                            isSelected = item.name == selectedSource.value,
                                            onClick = {
                                                selectedSource.value = item.name
                                                onEventDispatcher(
                                                    NewsContract.Intent.ClickedSource(
                                                        item.id
                                                    )
                                                )
                                            },
                                            colorBoxSelected = MaterialTheme.colorScheme.surfaceTint,
                                            colorBoxUnSelected = MaterialTheme.colorScheme.surfaceDim,
                                            colorText = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Latest News",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .align(
                                            Alignment.CenterStart
                                        )
                                )
                                Image(
                                    painter = painterResource(
                                        if (uiState.isFavoriteItems) R.drawable.icon_favourites_cliced
                                        else R.drawable.icon_favourites
                                    ),
                                    contentDescription = "Filter",
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(horizontal = 24.dp)
                                        .size(24.dp)
                                        .clickable {
                                            onEventDispatcher(NewsContract.Intent.FavoriteItemsClicked)
                                        }
                                )

                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    if (uiState.isLoading) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }

                        }
                    } else {
                        items(uiState.articles) { newsItem ->
                            Column(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.surface)
                            ) {
                                NewsCard(
                                    newsItem = newsItem,
                                    colorBox = MaterialTheme.colorScheme.onSecondary,
                                    colorTitle = MaterialTheme.colorScheme.secondary,
                                    descriptionColor = MaterialTheme.colorScheme.outline,
                                    buttonBoxColor = MaterialTheme.colorScheme.inversePrimary,
                                    buttonTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                                    onClick = { navigator.push(NewsDetails(newsItem)) }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                        }
                    }
                }
            }
        }
    }
}