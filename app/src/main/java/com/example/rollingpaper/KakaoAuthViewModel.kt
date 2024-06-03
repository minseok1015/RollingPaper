package com.example.rollingpaper

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class KakaoAuthViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "KakaoAuthViewModel"
    }

    private val context = application.applicationContext

    private val _friends = MutableStateFlow<List<String>>(emptyList())
    val friends: StateFlow<List<String>> = _friends

    fun handleKakaoLogin(callback: (User?) -> Unit) {
        val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                fetchProfile(callback)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    fetchProfile(callback)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoCallback)
        }
    }

    private fun fetchProfile(callback: (User?) -> Unit) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
                callback(null)
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공")
                Log.i(TAG, "ID: ${user.id}")
                Log.i(TAG, "닉네임: ${user.kakaoAccount?.profile?.nickname}")
                Log.i(TAG, "프로필 이미지: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                callback(user)
            }
        }
    }

    fun fetchFriends() {
        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(TAG, "친구 목록 요청 실패", error)
                _friends.value = emptyList()
            } else if (friends != null) {
                val friendIds = friends.elements?.map { it.uuid } ?: emptyList()
                Log.i(TAG, "친구 목록 요청 성공: $friendIds")
                _friends.value = friendIds
            }
        }
    }

    fun sendMessage(friendIds: List<String>, message: String) {
        // 메시지 전송 로직 구현
        // 여기에 친구 목록에 있는 사용자들에게 메시지를 전송하는 로직을 추가하세요.
    }
}
