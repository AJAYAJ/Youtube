package com.myworks.youtubeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myworks.youtubeapp.ui.theme.AppDarkBlue
import com.myworks.youtubeapp.ui.theme.AppLightGray

data class ChannelItem(val name: String, val color: Color)

@Composable
fun ChannelGrid(
    channelNames: List<String>,
    onChannelClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 0.dp
) {
    val colors = listOf(
        Color(0xFF2196F3), Color(0xFFF44336), Color(0xFFFFC107),
        Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF00BCD4), Color(0xFF673AB7), Color(0xFF607D8B)
    )

    val channels = remember(channelNames) {
        channelNames.mapIndexed { index, name ->
            ChannelItem(name, colors[index % colors.size])
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp + contentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(AppLightGray.copy(alpha = 0.3f))
    ) {
        items(channels) { channel ->
            ChannelGridItem(channel) {
                onChannelClick(channel.name)
            }
        }
    }
}

@Composable
fun ChannelGridItem(channel: ChannelItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(channel.color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = channel.name.take(1),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = channel.color
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = channel.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = AppDarkBlue,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChannelGridPreview() {
    ChannelGrid(channelNames = listOf("MrBeast", "PewDiePie"), onChannelClick = {})
}
