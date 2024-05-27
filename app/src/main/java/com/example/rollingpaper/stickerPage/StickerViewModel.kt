package com.example.rollingpaper

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class StickerViewModel(private val application: Application) : AndroidViewModel(application) {


    var show = mutableStateOf(false)
        private set
    var completeShow = mutableStateOf(false)
        private set
    var emoticonShow = mutableStateOf(false)
        private set

    //all selected stickers until now
    var selectedArray = mutableStateListOf<SelectedSticker>()

    //sticker chosen right now
    lateinit var nowSticker: SelectedSticker
//    var deletableList = mutableListOf<Boolean>()


    fun changeShow() {
        show.value = !show.value
    }
    fun changeCompleteShow() {
        completeShow.value = !completeShow.value
    }
    fun changeEmoticonShow() {
        emoticonShow.value = !emoticonShow.value
    }

    fun toggleDeleteButton(idx: Int) {
        selectedArray[idx] = selectedArray[idx].copy(deletable = !selectedArray[idx].deletable)
        print(selectedArray[idx].deletable)
    }

    fun removeSticker(idx: Int) {
        selectedArray.removeAt(idx)
    }
}