package com.example.rollingpaper

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MemoViewModelFactory(private val application: Application,private val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            return MemoViewModel(application,repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MemoViewModel(private val application: Application,private val repository: Repository): AndroidViewModel(application) {

    private var _memoList = MutableStateFlow<List<Memo>>(emptyList())
    val memoList = _memoList.asStateFlow()

    private var _stickerList = MutableStateFlow<List<SelectedSticker>>(emptyList())
    val stickerList = _stickerList.asStateFlow()
    fun insertMemo(memo: Memo){
        viewModelScope.launch {
            repository.insertMemo(memo)
        }
    }

    fun insertSticker(pageId:String, stickerId:String, x: Float, y:Float){
        viewModelScope.launch {
            repository.insertSticker(pageId, stickerId, x, y)
        }
    }

    fun deleteSticker(pageId:String, stickerId:String) {
        viewModelScope.launch {
            repository.deleteSticker(pageId, stickerId)
        }
    }


    fun getAllStickers(pageId: String, context: Context) {
        viewModelScope.launch {
            repository.getAllStickers(pageId, context).collect{
                _stickerList.value = it
            }
        }
    }

    fun getAllMemos(){
        viewModelScope.launch {
            repository.getAllMemos().collect{
                _memoList.value=it
            }
        }
    }

}

