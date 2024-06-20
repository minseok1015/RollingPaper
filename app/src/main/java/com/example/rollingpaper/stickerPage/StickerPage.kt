package com.example.rollingpaper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rollingpaper.StickerViewModel
import com.kakao.sdk.friend.m.s

@Composable
fun StickerPage(context: Context, stickerViewModel: StickerViewModel = viewModel()) {
    val typedArray = context.resources.obtainTypedArray(R.array.stickerImages)
    val stickerList = mutableListOf<Drawable?>()
//    var selectedSticker by remember {
//        mutableStateOf<Drawable?>(null)
//    }

    for (i in 0 until typedArray.length()) {
        val drawableResId = typedArray.getResourceId(i, -1)
//        Log.d("mymyss", drawableResId.toString())
        if (drawableResId != -1) {
            stickerList.add(ContextCompat.getDrawable(context, drawableResId))
//            stickerList.add(painterResource(id = ))
        }
    }
//    val drawableResId = typedArray.getResourceId(0, -1)
//    stickerList.add(ContextCompat.getDrawable(context, drawableResId))

    typedArray.recycle()

    Column(modifier = Modifier.background(Color.White)) {
        Row {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            stickerViewModel.changeShow()
                        }
                )
            }

        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)

        ) {
            itemsIndexed(items = stickerList) { idx, sticker ->
                sticker?.let {
                    val bitmap = it.toBitmap()
                    Image(
                        painter = BitmapPainter(bitmap.asImageBitmap()),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable {
                                stickerViewModel.nowSticker = SelectedSticker(it, 400f, 1000f, id="")
                                stickerViewModel.stickerId = "${idx}X"
                                stickerViewModel.changeCompleteShow()
                                stickerViewModel.changeShow()
                                stickerViewModel.changeEmoticonShow()
                            }
                            .aspectRatio(1f)
                    )
                }

            }
        }
    }

}