package com.example.rollingpaper

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MemoViewModelFactory(private val repository: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemoViewModel::class.java)) {
            return MemoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MemoViewModel(private val application: Application): AndroidViewModel(application) {
    var memoList= mutableStateListOf<Memo>()
    init{
        memoList.add(Memo(1,"모프5조 화이팅","김민석",1,20, 1, 3,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3",2,20, 1, 3,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석",1,20, 1, 2,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3",1,20, 1, 2,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석",2,20, 1, 3,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3",2,20, 1, 2,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석",1,20, 1, 3,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3",2,20, 1, 2,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석",1,20, 1, 3,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3",2,20, 1, 2,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석",2,20, 1, 3,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3",2,20, 1, 2,8,1))

    }

}

