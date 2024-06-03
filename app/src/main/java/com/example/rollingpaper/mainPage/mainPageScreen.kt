package com.example.rollingpaper.mainPage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.MainScreen
import com.example.rollingpaper.Memo
import com.example.rollingpaper.MemoViewModel
import com.example.rollingpaper.Routes
import com.example.rollingpaper.StickerViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageScreen(navController: NavController, stickerViewModel: StickerViewModel = viewModel()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val kakaoAuthViewModel: KakaoAuthViewModel = viewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(Color.White),
                content = {
                    DrawerContent()
                }
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.32f) // 스크림 색상 설정
    ) {
        Scaffold(
            topBar = { TopBar(
                onMenuClick = { scope.launch { drawerState.open() } },
                onShareClick = {
                    kakaoAuthViewModel.handleKakaoLogin { user ->
                        user?.let {
                            kakaoAuthViewModel.fetchFriends()
                        }
                    }
                }
            ) },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Routes.Memo.route) },
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "글쓰기")
                    }
                    FloatingActionButton(
                        onClick = {
                            stickerViewModel.changeShow()
                            stickerViewModel.changeEmoticonShow()
                        },
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Outlined.Face, contentDescription = "스티커")
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                val memoModel = viewModel<MemoViewModel>()
                val chunkedItems = memoModel.memoList.chunked(2)

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(chunkedItems) { _, rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (item in rowItems) {
                                MemoItem(MemoContents = item, modifier = Modifier.weight(1f)) {
                                    // onClick 핸들러
                                }
                            }
                            if (rowItems.size < 2) {
                                for (i in rowItems.size until 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val friends by kakaoAuthViewModel.friends.collectAsState()
    if (friends.isNotEmpty()) {
        FriendSelectionDialog(friends) { selectedFriends ->
            kakaoAuthViewModel.sendMessage(selectedFriends, "안녕하세요! 이 메시지는 테스트입니다.")
            Toast.makeText(navController.context, "메시지를 보냈습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onMenuClick: () -> Unit, onShareClick: () -> Unit) {
    TopAppBar(
        title = { Text("To. 미니", fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Share"
                )
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "로그인하세요", modifier = Modifier.padding(16.dp))
        Divider()
        Text(text = "홈", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        Text(text = "마이페이지", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        Text(text = "Team5", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        Text(text = "팀원 소개", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoItem(MemoContents: Memo, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val rotationAngle = Random.nextFloat() * 10 - 5 // -5도에서 5도 사이의 랜덤 각도
    var likes by remember { mutableStateOf(MemoContents.like) }

    Box(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .graphicsLayer(rotationZ = rotationAngle)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MemoContents.memoColor) // memoColor를 배경색으로 지정
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = MemoContents.content, color = MemoContents.fontColor)
                    Text(text = MemoContents.name, color = MemoContents.fontColor)
                }
            }
            BadgedBox(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-8).dp, y = 8.dp),
                badge = {
                    Badge {
                        Text(text = "$likes")
                    }
                }
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.clickable { likes++ }
                )
            }
        }
    }
}

@Composable
fun FriendSelectionDialog(friends: List<String>, onConfirm: (List<String>) -> Unit) {
    var selectedFriends by remember { mutableStateOf(emptyList<String>()) }
    val onFriendClick = { friend: String ->
        selectedFriends = if (selectedFriends.contains(friend)) {
            selectedFriends - friend
        } else {
            selectedFriends + friend
        }
    }

    AlertDialog(
        onDismissRequest = { /* Dialog 닫기 로직 */ },
        title = { Text(text = "친구 선택") },
        text = {
            LazyColumn {
                items(friends) { friend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onFriendClick(friend) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = friend)
                        Spacer(modifier = Modifier.weight(1f))
                        if (selectedFriends.contains(friend)) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = null)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedFriends) }) {
                Text(text = "확인")
            }
        },
        dismissButton = {
            TextButton(onClick = { /* Dialog 닫기 로직 */ }) {
                Text(text = "취소")
            }
        }
    )
}
