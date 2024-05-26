package com.example.rollingpaper.mainPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rollingpaper.Memo
import com.example.rollingpaper.MemoViewModel
import kotlin.random.Random

@Composable
fun MainPageScreen() {
    Column {
        val memoModel = viewModel<MemoViewModel>()
        val chunkedItems = memoModel.memoList.chunked(2)

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(chunkedItems) { _, rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (item in rowItems) {
                        MemoItem(MemoContents = item, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size < 2) {
                        for (i in rowItems.size until 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoItem(MemoContents: Memo, modifier: Modifier = Modifier) {
    val rotationAngle = Random.nextFloat() * 10 - 5 // -5도에서 5도 사이의 랜덤 각도
    var likes by remember { mutableStateOf(MemoContents.like) }

    Box(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .graphicsLayer(rotationZ = rotationAngle)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MemoContents.memoColor), // memoColor를 배경색으로 지정
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = MemoContents.content, color = MemoContents.fontColor)
                    Text(text = MemoContents.name, color = MemoContents.fontColor)
                }
            }
            BadgedBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp, y = 8.dp),
                badge = {
                    Badge {
                        Text(text = "$likes")
                    }
                }
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.clickable{
                        likes++
                        //DB에 좋아요 개수 증가하도록 요청하는 로직
                    }
                )
            }
        }
    }
}
