package com.example.rollingpaper.makeMemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rollingpaper.Memo
import com.example.rollingpaper.MemoViewModel
import com.example.rollingpaper.R
@Composable
fun makeMemoScreen(navController: NavController,viewModel: MemoViewModel) {
    var text by remember { mutableStateOf("") }
    var anonymous by remember { mutableStateOf(false) }
    val fontFamilies = listOf("굴림체", "궁서체", "바탕체", "고딕체")
    var selectedFont by remember { mutableStateOf(fontFamilies[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5EED3))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = "작성할 내용을 입력해 주세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "익명으로 작성하기",
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = anonymous,
                    onCheckedChange = { anonymous = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fontFamilies.forEach { font ->
                    Button(
                        onClick = { selectedFont = font },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedFont == font) Color.Gray else Color.LightGray
                        )
                    ) {
                        Text(text = font)
                    }
                }
            }
        }

        val memo = Memo(1,"데이터베이스 삽입테스트","민석",14,1,2,14,14,1)
        NavigationBar(
            containerColor = Color.LightGray,
            contentColor = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            NavigationBarItem(
                icon = { Icon(painter = painterResource(R.drawable.grr), contentDescription = "Text") },
                selected = false,
                onClick = { viewModel.insertMemo(memo) }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(R.drawable.grr), contentDescription = "Alignment") },
                selected = false,
                onClick = { /*TODO*/ }
            )
            NavigationBarItem(
                icon = { Icon(painter = painterResource(R.drawable.grr), contentDescription = "Colors") },
                selected = false,
                onClick = { /*TODO*/ }
            )
        }
    }
}
