package com.example.rollingpaper

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel

class MemoViewModel(private val application: Application): AndroidViewModel(application) {
    var memoList= mutableStateListOf<Memo>()
    init{
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림",20, 1, Color.White,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림",20, 1, Color.White,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림",20, 1, Color.Blue,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림",20, 1, Color.White,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림",20, 1, Color.LightGray,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림",20, 1, Color.Cyan,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림",20, 1, Color.Green,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림",20, 1, Color.White,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림",20, 1, Color.Magenta,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림",20, 1, Color.White,8,1))
        memoList.add(Memo(1,"모프5조 화이팅","김민석","굴림",20, 1, Color.Yellow,15,1))
        memoList.add(Memo(1,"과제 너무 많아요","익명3","굴림",20, 1, Color.White,8,1))

    }

}


// 정재우 푸쉬dd