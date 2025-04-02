package com.example.paymeinternapp.screens.news.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.domain.entities.ArticleUIData
import com.example.paymeinternapp.R
import com.example.paymeinternapp.screens.news.details.NewsDetails
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
    var selectedCategory = remember { mutableStateOf(uiState.categories[0]) }
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isLoading)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = {
//            onEventDispatcher()
        },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = swipeRefreshState,
                refreshTriggerDistance = 120.dp,
                backgroundColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = Color.LightGray
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            item {
                Column {
                    OutlinedTextField(
                        value = searchText.value,
                        onValueChange = {
                            searchText.value = it
                            onEventDispatcher(NewsContract.Intent.SearchByQuery(it))
                        },
                        placeholder = { Text("Search file...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(uiState.categories) { category ->
                            CategoryChip(
                                text = category,
                                isSelected = category == selectedCategory.value,
                                onClick = {
                                    selectedCategory.value = category
                                    onEventDispatcher(NewsContract.Intent.ClickedCategory(category))
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Latest News",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
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
                            .background(color = Color.LightGray)
                    ) {
                        NewsCard(newsItem = newsItem) {
                            navigator.push(NewsDetails(newsItem))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
            }
        }
    }
}


@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color.Blue else Color.LightGray)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsCard(newsItem: ArticleUIData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                GlideImage(
                    model = newsItem.urlToImage,
                    contentDescription = "Item Image",
                    modifier = Modifier.fillMaxSize()
                ) {
                    it.load(newsItem.urlToImage)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_no_image_available)
                        .fallback(R.drawable.ic_no_image_available)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = newsItem.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = newsItem.description ?: "",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                maxLines = 3
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "See more ...",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
