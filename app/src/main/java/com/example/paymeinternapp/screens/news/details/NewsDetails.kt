package com.example.paymeinternapp.screens.news.details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.ImageLoader
import coil3.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.domain.entities.ArticleUIData
import com.example.paymeinternapp.R
import com.example.paymeinternapp.screens.news.browser.OpenUrlScreen

class NewsDetails(private val data: ArticleUIData) : Screen {
    @Composable
    override fun Content() {
        NewsDetailsContent(article = data)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun NewsDetailsContent(modifier: Modifier = Modifier, article: ArticleUIData) {
    val navigator = LocalNavigator.currentOrThrow
    val imageLoader = ImageLoader.Builder(LocalContext.current)
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        Log.d("TTT", "NewsDetailsContent: pop")
                        navigator.pop()
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            GlideImage(
                model = article.urlToImage,
                contentDescription = "Item Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                it.load(article.urlToImage)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_no_image_available)
                    .fallback(R.drawable.ic_no_image_available)
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = article.title ?: "",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = article.description ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.author ?: "",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "–",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                    Text(
                        text = article.publishedAt ?: "",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = article.content ?: "",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )
                Text(
                    text = "link",
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navigator.push(OpenUrlScreen(article.url ?: ""))
                    })
            }
        }
    }
}
