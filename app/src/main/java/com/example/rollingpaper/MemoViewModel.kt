package com.example.rollingpaper

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel

class MemoViewModel(private val application: Application): AndroidViewModel(application) {
    var memoList= mutableStateListOf<Memo>()
    init{

    }

}


// 정재우 푸쉬dd