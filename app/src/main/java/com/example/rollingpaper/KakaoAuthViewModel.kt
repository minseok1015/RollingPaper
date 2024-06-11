package com.example.rollingpaper

import android.annotation.SuppressLint
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.friend.client.PickerClient
import com.kakao.sdk.friend.model.OpenPickerFriendRequestParams
import com.kakao.sdk.friend.model.PickerOrientation
import com.kakao.sdk.friend.model.ViewAppearance
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import com.kakao.sdk.template.model.Social
import com.kakao.sdk.user.UserApiClient
import java.util.Date

@SuppressLint("StaticFieldLeak")
class KakaoAuthViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "KakaoAuthViewModel"
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

    fun checkLoginStatus() {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                _isLoggedIn.value = false
                Log.e(TAG, "로그인 상태 확인 실패", error)
            } else if (tokenInfo != null) {
                _isLoggedIn.value = true
                Log.i(TAG, "로그인 상태 확인 성공" + tokenInfo)
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

    val context = application.applicationContext

    fun handleKakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                saveToken(token)

                val scopes = listOf("account_email")
                UserApiClient.instance.loginWithNewScopes(context, scopes) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오 로그인 실패", error)
                    } else {
                        Log.i(TAG, "카카오 로그인 성공")

                        UserApiClient.instance.me { user, meError ->
                            if (meError != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", meError)
                            } else if (user != null) {
                                Log.i(TAG, "사용자 정보 요청 성공: ${user.kakaoAccount}")

                                Log.i(TAG, "회원번호: ${user.id}")
                                Log.i(TAG, "이메일: ${user.kakaoAccount?.email}")
                                Log.i(TAG, "닉네임: ${user.kakaoAccount?.profile?.nickname}")
                                Log.i(TAG, "프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                            }
                        }
                    }
                }

                _isLoggedIn.value = true
                _loginEvent.value = Event(true)
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
                    saveToken(token)

                    UserApiClient.instance.me { user, userError ->
                        if (userError != null) {
                            Log.e(TAG, "사용자 정보 가져오기 실패", userError)
                        } else if (user != null) {
                            Log.i(TAG, "사용자 정보: ${user.kakaoAccount?.email}")
                        }
                    }

                    _isLoggedIn.value = true
                    _loginEvent.value = Event(true)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    // 공유 함수
    fun shareContent(context: Context,title: String, description: String, imageUrl: String, webUrl: String) {
        // 피드 메시지 보내기
        val defaultFeed = FeedTemplate(
            content = Content(
                title = "오늘의 디저트",
                description = "#케익 #딸기 #삼평동 #카페 #분위기 #소개팅",
                imageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                link = Link(
                    webUrl = "https://developers.kakao.com",
                    mobileWebUrl = "https://developers.kakao.com"
                )
            ),
            itemContent = ItemContent(
                profileText = "Kakao",
                profileImageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                titleImageUrl = "https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                titleImageText = "Cheese cake",
                titleImageCategory = "Cake",
                items = listOf(
                    ItemInfo(item = "cake1", itemOp = "1000원"),
                    ItemInfo(item = "cake2", itemOp = "2000원"),
                    ItemInfo(item = "cake3", itemOp = "3000원"),
                    ItemInfo(item = "cake4", itemOp = "4000원"),
                    ItemInfo(item = "cake5", itemOp = "5000원")
                ),
                sum = "Total",
                sumOp = "15000원"
            ),
            social = Social(
                likeCount = 286,
                commentCount = 45,
                sharedCount = 845
            ),
            buttons = listOf(
                Button(
                    "웹으로 보기",
                    Link(
                        webUrl = "https://developers.kakao.com",
                        mobileWebUrl = "https://developers.kakao.com"
                    )
                ),
                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                        iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
                    )
                )
            )
        )

        // 카카오톡 설치여부 확인
        if (ShareClient.instance.isKakaoTalkSharingAvailable(context)) {
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(context, defaultFeed) { sharingResult, error ->
                if (error != null) {
                    Log.e(KakaoAuthViewModel.TAG, "카카오톡 공유 실패", error)
                } else if (sharingResult != null) {
                    Log.d(KakaoAuthViewModel.TAG, "카카오톡 공유 성공 ${sharingResult.intent}")
                    sharingResult.intent?.let {
                        ContextCompat.startActivity(context, it, null)
                    }

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Log.w(KakaoAuthViewModel.TAG, "Warning Msg: ${sharingResult.warningMsg}")
                    Log.w(KakaoAuthViewModel.TAG, "Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(context, sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(context, sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }


    // 친구 목록 가져오기, 친구 선택하기
//    fun selectFriends() {
//        val openPickerFriendRequestParams = OpenPickerFriendRequestParams(
//            title = "풀 스크린 멀티 친구 피커", //default "친구 선택"
//            viewAppearance = ViewAppearance.AUTO, //default ViewAppearance.AUTO
//            orientation = PickerOrientation.AUTO, //default PickerOrientation.AUTO
//            enableSearch = true, //default true
//            enableIndex = true, //default true
//            showMyProfile = true, //default true
//            showFavorite = true, //default true
//            showPickedFriend = null, // default true
//            maxPickableCount = null, // default 30
//            minPickableCount = null // default 1
//        )
//
//        PickerClient.instance.selectFriends(
//            context = context!!,
//            params = openPickerFriendRequestParams
//        ) { selectedUsers, error ->
//            if (error != null) {
//                Log.e(TAG, "친구 선택 실패", error)
//            } else {
//                Log.d(TAG, "친구 선택 성공 $selectedUsers")
//            }
//        }
//    }
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
