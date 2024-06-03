    package com.example.rollingpaper.homePage

    import android.content.ContentValues.TAG
    import android.util.Log
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.Button
    import androidx.compose.material3.ButtonDefaults
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavController
    import com.example.rollingpaper.Routes
    import com.kakao.sdk.user.UserApiClient


    @Composable
    fun homeScreen(navController: NavController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5EED3))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Rolling Paper",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 50.dp)
                )

                Button(
                    onClick = { navController.navigate(Routes.Page.route)
                              },
                    colors = ButtonDefaults.buttonColors( Color(0xFF3C352E)),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "시작하기", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = { navController.navigate(Routes.MakePage.route)},
                    colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "페이지 생성", color = Color.White, fontSize = 18.sp)
                }

                Button(
                    onClick = {// 로그아웃
                        UserApiClient.instance.logout { error ->
                            if (error != null) {
    //                            var TAG = null
                                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                            }
                            else {
                                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                            }
                        }
                         },
                    colors = ButtonDefaults.buttonColors(Color(0xFF3C352E)),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "로그아웃", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
