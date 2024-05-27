package com.example.rollingpaper

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rollingpaper.SelectedSticker
import com.example.rollingpaper.StickerPage
import com.example.rollingpaper.StickerViewModel
import kotlin.math.roundToInt

@Composable
fun MainScreen(stickerViewModel: StickerViewModel = viewModel()) {
    var offsetX by remember { mutableStateOf(400f) }
    var offsetY by remember { mutableStateOf(1000f) }
    val context = LocalContext.current

    Box {

        if (!stickerViewModel.selectedArray.isEmpty()) {

            Box {
                stickerViewModel.selectedArray.mapIndexed { idx, sticker ->
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    sticker.offsetX.roundToInt(),
                                    sticker.offsetY.roundToInt()
                                )
                            }
                            .height(130.dp)
                            .width(130.dp)
                            .clickable {
                                stickerViewModel.toggleDeleteButton(idx)
                            }
                    ) {
                        Image(
                            painter = BitmapPainter(
                                sticker.sticker?.toBitmap()!!.asImageBitmap()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(130.dp)
                                .padding(top = 15.dp)

                        )
                        if (sticker.deletable) {
                            IconButton(
                                onClick = {
                                    stickerViewModel.removeSticker(idx)
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(100.dp)
                                )

                            }
                        }

                    }
                }
            }
        }
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
                    stickerViewModel.selectedArray.add(
                        SelectedSticker(
                            stickerObject.sticker,
                            offsetX,
                            offsetY
                        )
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
            StickerPage(context)
        }

    }
}