package com.example.rollingpaper.mainPage

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.MainScreen
import com.example.rollingpaper.Memo
import com.example.rollingpaper.MemoViewModel
import com.example.rollingpaper.StickerViewModel
import com.example.rollingpaper.component.Colors
import com.example.rollingpaper.component.FontColors
import com.example.rollingpaper.component.Fonts
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageScreen(pageId: String?, title: String?, theme: Int?, navController: NavController, memoModel: MemoViewModel, stickerViewModel: StickerViewModel = viewModel(), kakaoAuthViewModel: KakaoAuthViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val scope = rememberCoroutineScope()
//    val lazyListState = rememberLazyListState()
//    val table= Firebase.database.getReference("Pages/memos")
//    val memoModel: MemoViewModel = viewModel(factory = MemoViewModelFactory(application, Repository(table)))
    val memoList by memoModel.memoList.collectAsState(initial = emptyList())

    val themeColor = when (theme) {
        1 -> Color(0xFFD7FBE8)
        2 -> Color(0xFFD7E8FB)
        3 -> Color(0xFFFBD7D7)
        else -> Color.White
    }

//    val scrollOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }
    LaunchedEffect(pageId) {
        pageId?.let {
            memoModel.getAllMemos(it)
        }
    }

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
            topBar = { TopBar(onMenuClick = { scope.launch { drawerState.open() } }, kakaoAuthViewModel, pageId) },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            pageId?.let {
                                val route = "Memo/$it?title=$title&theme=$theme"
                                navController.navigate(route)
                            }
                        },
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Outlined.Edit, contentDescription = "글쓰기")
                    }
                    FloatingActionButton(
                        onClick = {
                            stickerViewModel.changeShow()
                            Log.d("mine", stickerViewModel.show.value.toString())
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
                    .background(themeColor)
            ) {
                Text("페이지 ID: $pageId", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("제목: $title", fontSize = 18.sp, fontWeight = FontWeight.Medium)

//                val memoModel = viewModel<MemoViewModel>()
//                val chunkedItems = memoModel.memoList.chunked(2)


                val listState = rememberLazyListState()
//                val itemHeight = with(LocalDensity.current) { 300.dp.toPx() } // Your item height
                val firstVisibleItemIndex = listState.firstVisibleItemIndex
                val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
//                val totalOffsetInPx = firstVisibleItemIndex * 150 + firstVisibleItemScrollOffset
                val scrollDp =
                    with(LocalDensity.current) { (firstVisibleItemIndex * 180).dp + firstVisibleItemScrollOffset.toDp() }
                var imageOffset by remember { mutableStateOf(Offset.Zero) }
//                Text("dp : $dpjb")
                Box(
                
                ) {
                    val chunkedItems = memoList.chunked(2)

                    LazyColumn(
                        state = listState,
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
                    stickerViewModel.selectedArray.mapIndexed { idx, sticker ->
                        var xdp = with(LocalDensity.current) { sticker.offsetX.toDp() }
                        var ydp = with(LocalDensity.current) { sticker.offsetY.toDp() }
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = xdp,
                                    y = ydp - scrollDp
                                )
                                .height(130.dp)
                                .width(130.dp)
                                .clickable {
                                    stickerViewModel.toggleDeleteButton(idx)
                                }
                                .onGloballyPositioned { layoutCoordinates ->
                                    imageOffset = layoutCoordinates.positionInRoot()
                                }
                        ) {
                            Image(
                                painter = BitmapPainter(
                                    sticker.sticker?.toBitmap()!!.asImageBitmap()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(130.dp)
                                    .padding(top = 15.dp)

                            )
                            if (sticker.deletable) {
                                IconButton(
                                    onClick = {
                                        stickerViewModel.removeSticker(idx)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(100.dp)
                                    )

                                }
                            }

                        }
                        LaunchedEffect(imageOffset) {
                            println("Image offset: $imageOffset")

                        }
                    }
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onMenuClick: () -> Unit, viewModel: KakaoAuthViewModel, pageId: String?) {
    TopAppBar(
        title = { Text("To. 미니", fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = {
                // 공유하기 로직
                viewModel.openPicker(pageId)
            }) {
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
        ))
    }

    @Composable
    fun DrawerContent() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "로그인하세요", modifier = Modifier.padding(16.dp))
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
                    colors = CardDefaults.cardColors(containerColor = Colors.getColorByIndex(MemoContents.memoColor)) // memoColor를 배경색으로 지정
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = MemoContents.content,
                        color = FontColors.getFontColorByIndex(MemoContents.fontColor),
                        fontFamily = Fonts.getFontByIndex(MemoContents.font)
                    )
                    Text(text = MemoContents.name,
                        color =  FontColors.getFontColorByIndex(MemoContents.fontColor),
                        fontFamily = Fonts.getFontByIndex(MemoContents.font)
                    )
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
    }


