package com.example.rollingpaper

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel

class MemoViewModel(private val application: Application): AndroidViewModel(application) {
    var memoList= mutableStateListOf<Memo>()
    init{
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림","20","빨강","white",15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림","20","빨강","white",15,1))

    }

}
