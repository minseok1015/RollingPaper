package com.example.rollingpaper

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

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

    var stickerId = "0"

    init{
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
    }
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
        Log.d("mymy", idx.toString())
        selectedArray.removeAt(idx)

    }
}