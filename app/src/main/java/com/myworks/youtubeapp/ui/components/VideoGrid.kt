package com.myworks.youtubeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.myworks.youtubeapp.models.VideoItemData
import com.myworks.youtubeapp.ui.theme.AppDarkBlue
import com.myworks.youtubeapp.ui.theme.AppLightGray
import java.util.concurrent.TimeUnit

@Composable
fun VideoGrid(
    videoIds: List<String>,
    videoData: List<VideoItemData>,
    onVideoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 0.dp
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp + contentPadding),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(AppLightGray.copy(alpha = 0.3f))
    ) {
        items(videoIds.take(videoData.size).zip(videoData)) { (id, data) ->
            VideoGridItem(videoId = id, data = data) {
                onVideoClick(id)
            }
        }
    }
}

@Composable
fun VideoGridItem(videoId: String, data: VideoItemData, onClick: () -> Unit) {
    val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    val formattedTime = remember(data.timestamp) { formatMillis(data.timestamp) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(data.color)
        ) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = data.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = android.R.drawable.ic_menu_gallery),
                error = painterResource(id = android.R.drawable.ic_menu_report_image)
            )

            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Text(
                    text = formattedTime,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = data.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = AppDarkBlue
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = " $formattedTime  ",
                fontSize = 10.sp,
                color = Color.Gray
            )
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_view),
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = " ${data.views}",
                fontSize = 10.sp,
                color = Color.Gray
            )
        }
    }
}

private fun formatMillis(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d:%02d", minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun VideoGridItemPreview() {
    VideoGridItem(
        videoId = "gyhl3UHFWHE",
        data = VideoItemData("gyhl3UHFWHE", "Preview Title", "Keywords", 605000L, "100K", Color.Blue, "Story", "Channel"),
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun VideoGridPreview() {
    VideoGrid(
        videoIds = listOf("gyhl3UHFWHE", "gd141sa3-mM"),
        videoData = listOf(
            VideoItemData("gyhl3UHFWHE", "Title 1", "Keywords 1", 15000L, "43K", Color.Red, "Story 1", "Channel 1"),
            VideoItemData("gd141sa3-mM", "Title 2", "Keywords 2", 495000L, "25K", Color.Green, "Story 2", "Channel 2")
        ),
        onVideoClick = {}
    )
}
