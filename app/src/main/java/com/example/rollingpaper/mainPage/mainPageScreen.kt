package com.example.rollingpaper.mainPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        val chunkedItems = memoModel.memoList.chunked(3)

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
                    // 빈 카드를 채워넣어서 Row가 꽉 차도록 함
                    if (rowItems.size < 3) {
                        for (i in rowItems.size until 3) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemoItem(MemoContents: Memo, modifier: Modifier = Modifier) {
    val rotationAngle = Random.nextFloat() * 10 - 5 // -5도에서 5도 사이의 랜덤 각도

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(150.dp)
            .graphicsLayer(rotationZ = rotationAngle),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = MemoContents.content)
            Text(text = MemoContents.name, style = MaterialTheme.typography.bodySmall)
        }
    }
}
