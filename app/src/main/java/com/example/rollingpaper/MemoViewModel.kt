package com.example.rollingpaper

import android.app.Application
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
    fun insertMemo(memo: Memo){
        viewModelScope.launch {
            repository.insertMemo(memo)
        }
    }

}

