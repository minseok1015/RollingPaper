package com.example.rollingpaper

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class Kakao : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "be4367df57d1b4431763aef17ad9467d")
    }
}