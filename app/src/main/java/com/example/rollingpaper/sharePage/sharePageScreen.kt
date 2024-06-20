package com.example.rollingpaper.sharePage

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.R

@Composable
fun sharePageScreen(navController: NavController, viewModel: KakaoAuthViewModel, pageId: String) {
    val context = LocalContext.current
    val backgroundPainter: Painter = painterResource(id = R.drawable.paper_background)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color(0xFF3E2723)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "당신의 페이지를 친구와\n공유해보세요!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
                modifier = Modifier
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "초대코드",
                fontSize = 32.sp, // 텍스트 크기 증가
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pageId,
                    fontSize = 28.sp, // 텍스트 크기 증가
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_copy2),
                    contentDescription = "초대코드 복사",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            copyToClipboard(context, pageId)
                            Toast.makeText(context, "초대코드가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            val kakaoShareImage: Painter = painterResource(id = R.drawable.kakaoshare)
            Image(
                painter = kakaoShareImage,
                contentDescription = "카카오톡 공유하기",
                modifier = Modifier
                    .size(64.dp) // 이미지 크기 설정
                    .clickable { viewModel.openPicker(pageId) }
            )
        }
    }
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("초대코드", text)
    clipboard.setPrimaryClip(clip)
}
