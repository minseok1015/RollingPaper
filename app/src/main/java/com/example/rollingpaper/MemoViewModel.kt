package com.example.rollingpaper

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MemoViewModelFactory(private val application: Application, private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            return MemoViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MemoViewModel(private val application: Application, private val repository: Repository) : AndroidViewModel(application) {
    private var currentMemoId = 0

    init {
        viewModelScope.launch {
            currentMemoId = repository.getCurrentMemoId()
        }
    }

    fun getNextMemoId(): Int {
        currentMemoId++
        return currentMemoId
    }

    private var _memoList = MutableStateFlow<List<Memo>>(emptyList())
    val memoList = _memoList.asStateFlow()

    private var _stickerList = MutableStateFlow<List<SelectedSticker>>(emptyList())
    val stickerList = _stickerList.asStateFlow()
    fun insertMemo(pageId: String, memo: Memo) {
        viewModelScope.launch {
            repository.insertMemo(pageId, memo,currentMemoId)
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
            repository.getAllStickers(pageId, context).collect {
                _stickerList.value = it.distinctBy { sticker -> sticker.id }
            }
        }
    }

    fun getAllMemos(pageId: String) {
        viewModelScope.launch {
            repository.getAllMemos(pageId).collect {
                _memoList.value = it
            }
        }
    }


    
    fun getPageInfo(pageId: String, onSuccess: (Page) -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            repository.getPageInfo(pageId).collect { page ->
                page?.let(onSuccess) ?: onError(Exception("Page not found"))
            }
        }
    }

    fun increaseLike(pageId: String, memoId: Int) {
        viewModelScope.launch {
            repository.increaseLike(pageId, memoId)
        }
    }
}

