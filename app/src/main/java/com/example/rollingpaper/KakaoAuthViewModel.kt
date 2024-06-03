package com.example.rollingpaper
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import java.util.Date

class KakaoAuthViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "KakaoAuthViewModel"
    }

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("kakao_auth_prefs", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    private val _loginEvent = MutableLiveData<Event<Boolean>>()
    val loginEvent: LiveData<Event<Boolean>> get() = _loginEvent

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                _isLoggedIn.value = false
                Log.e(TAG, "로그인 상태 확인 실패", error)
            } else if (tokenInfo != null) {
                _isLoggedIn.value = true
                Log.i(TAG, "로그인 상태 확인 성공")
            }
        }
    }

    fun logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                if (error is ClientError && error.reason == ClientErrorCause.TokenNotFound) {
                    _isLoggedIn.value = false
                }
            } else {
                clearToken()
                _isLoggedIn.value = false
                Log.i(TAG, "로그아웃 성공")
            }
        }
    }

    private fun clearToken() {
        sharedPreferences.edit().clear().apply()
    }

    private fun saveToken(token: OAuthToken) {
        Log.i(TAG, "토큰 저장: accessToken=${token.accessToken}, refreshToken=${token.refreshToken}")
        sharedPreferences.edit().apply {
            putString("access_token", token.accessToken)
            putString("refresh_token", token.refreshToken)
            putLong("access_token_expires_at", token.accessTokenExpiresAt.time)
            putLong("refresh_token_expires_at", token.refreshTokenExpiresAt.time)
            apply()
        }
    }

    private fun getToken(): OAuthToken? {
        val accessToken = sharedPreferences.getString("access_token", null)
        val refreshToken = sharedPreferences.getString("refresh_token", null)
        val accessTokenExpiresAtMillis = sharedPreferences.getLong("access_token_expires_at", -1)
        val refreshTokenExpiresAtMillis = sharedPreferences.getLong("refresh_token_expires_at", -1)

        Log.i(TAG, "토큰 불러오기: accessToken=$accessToken, refreshToken=$refreshToken")

        return if (accessToken != null && refreshToken != null &&
            accessTokenExpiresAtMillis != -1L && refreshTokenExpiresAtMillis != -1L) {
            OAuthToken(
                accessToken = accessToken,
                refreshToken = refreshToken,
                accessTokenExpiresAt = Date(accessTokenExpiresAtMillis),
                refreshTokenExpiresAt = Date(refreshTokenExpiresAtMillis)
            )
        } else {
            null
        }
    }

    private val context = application.applicationContext

    fun handleKakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                saveToken(token) // 토큰 저장

                // 사용자 정보 가져오기
                UserApiClient.instance.me { user, userError ->
                    if (userError != null) {
                        Log.e(TAG, "사용자 정보 가져오기 실패", userError)
                    } else if (user != null) {
                        val email = user.kakaoAccount?.email
                        if (email != null) {
                            Log.i(TAG, "사용자 정보: $email")
                        } else {
                            Log.w(TAG, "사용자 이메일 정보가 없습니다.")
                        }
                    }
                }

                _isLoggedIn.value = true
                _loginEvent.value = Event(true) // 로그인 이벤트 트리거
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    saveToken(token) // 토큰 저장

                    // 사용자 정보 가져오기
                    UserApiClient.instance.me { user, userError ->
                        if (userError != null) {
                            Log.e(TAG, "사용자 정보 가져오기 실패", userError)
                        } else if (user != null) {
                            Log.i(TAG, "사용자 정보: ${user.kakaoAccount?.email}") // 사용자 이메일 출력
                        }
                    }

                    _isLoggedIn.value = true
                    _loginEvent.value = Event(true) // 로그인 이벤트 트리거
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

}

// 이벤트를 처리하기 위한 헬퍼 클래스
open class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}
