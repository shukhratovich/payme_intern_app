package com.example.paymeinternapp.screens.news.categories

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.rememberAsyncImagePainter
import com.example.domain.entities.ArticleUIData
import com.example.paymeinternapp.R
import com.example.paymeinternapp.screens.news.details.NewsDetails

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

        items(uiState.articles) { newsItem ->
            NewsCard(newsItem = newsItem) {
                navigator.push(NewsDetails(newsItem))
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                if (newsItem.url.isEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_placeholder),
                        contentDescription = "News image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = newsItem.urlToImage,
                            onLoading = { test ->

                            },
                            onError = {
                                R.drawable.ic_placeholder
                            }
                        ),
                        contentDescription = "News image",
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = newsItem.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Blue)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = newsItem.description,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}