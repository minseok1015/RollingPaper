package com.example.rollingpaper

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    stickerViewModel: StickerViewModel = viewModel(),
    memoModel: MemoViewModel,
    pageId: String
) {
    var offsetX by remember { mutableStateOf(400f) }
    var offsetY by remember { mutableStateOf(1000f) }
    val context = LocalContext.current
    var imageOffset by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier.fillMaxSize()) {
        Log.d("mymy", stickerViewModel.show.value.toString())
        if (stickerViewModel.completeShow.value) {
            val stickerObject = stickerViewModel.nowSticker
            val bitmap = stickerObject.sticker?.toBitmap()
            Image(
                painter = BitmapPainter(bitmap!!.asImageBitmap()),
                contentDescription = "SelectedSticker",
                modifier = Modifier
                    .offset {
                        IntOffset(
                            offsetX.roundToInt(),
                            offsetY.roundToInt()
                        )
                    }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    }
                    .border(1.dp, Color.Black)
                    .size(100.dp)
            )

            ElevatedButton(
                onClick = {
                    stickerViewModel.changeCompleteShow()
//                    stickerViewModel.selectedArray.add(
//                        SelectedSticker(
//                            stickerObject.sticker,
//                            offsetX,
//                            offsetY,
//                            id = "${stickerViewModel.stickerId}${(offsetX * 100).toInt()}${(offsetY * 100).toInt()}"
//                        )
//                    )
                    memoModel.insertSticker(
                        pageId,
                        "${stickerViewModel.stickerId}${(offsetX * 100).toInt()}${(offsetY * 100).toInt()}",
                        offsetX,
                        offsetY
                    )
                    offsetX = 400f
                    offsetY = 1000f
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(200.dp)
                    .height(70.dp)
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    text = "완료"
                )
            }
        }
        if (stickerViewModel.show.value) {
            Log.d("mymy", "hello")
            StickerPage(context, stickerViewModel)
        }

    }
}