package com.example.paymeinternapp.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.domain.model.ui.NewsUIData
import com.example.paymeinternapp.R


@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colorBoxSelected: Color,
    colorBoxUnSelected: Color,
    colorText: Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) colorBoxSelected else colorBoxUnSelected)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = colorText,
            fontSize = 14.sp,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsCard(
    newsItem: NewsUIData,
    onClick: () -> Unit,
    colorBox: Color,
    colorTitle: Color,
    descriptionColor: Color,
    buttonBoxColor: Color,
    buttonTextColor: Color
) {
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
                    .background(colorBox)
            ) {
                GlideImage(
                    model = newsItem.urlToImage,
                    contentDescription = "Item Image",
                    transition = CrossFade,
                    modifier = Modifier.fillMaxSize()
                ) {
                    it.placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_no_image_available)
                        .fallback(R.drawable.ic_no_image_available)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = newsItem.title ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorTitle
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = newsItem.description ?: "",
                color = descriptionColor,
                fontWeight = FontWeight.Medium,
                maxLines = 3
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(buttonBoxColor)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "See more ...",
                    color = buttonTextColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
