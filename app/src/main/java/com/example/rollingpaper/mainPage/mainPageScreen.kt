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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Face
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.example.rollingpaper.KakaoAuthViewModel
import com.example.rollingpaper.MainScreen
import com.example.rollingpaper.Memo
import com.example.rollingpaper.MemoViewModel
import com.example.rollingpaper.R
import com.example.rollingpaper.Routes
import com.example.rollingpaper.StickerViewModel
import com.example.rollingpaper.component.Colors
import com.example.rollingpaper.component.FontColors
import com.example.rollingpaper.component.Fonts
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageScreen(
    pageId: String?,
    title: String?,
    theme: Int?,
    navController: NavController,
    memoModel: MemoViewModel,
    stickerViewModel: StickerViewModel,
    kakaoAuthViewModel: KakaoAuthViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val memoList by memoModel.memoList.collectAsState(initial = emptyList())

    val themeBackground = when (theme) {
        1 -> painterResource(id = R.drawable.theme1)
        2 -> painterResource(id = R.drawable.theme2)
        3 -> painterResource(id = R.drawable.theme3)
        else -> painterResource(id = R.drawable.theme1)
    }

    LaunchedEffect(pageId) {
        pageId?.let {
            memoModel.getAllMemos(it)
        }
        memoModel.getAllStickers(pageId!!, context)
        memoModel.stickerList.collect { it ->
            stickerViewModel.selectedArray.addAll(it)
        }
//    val scrollOffset by remember { derivedStateOf { lazyListState.firstVisibleItemScrollOffset } }
    LaunchedEffect(Unit) {
//        memoModel.getAllMemos()
        memoModel.getAllStickers(pageId!!, context)
//        stickerViewModel.selectedArray = memoModel.stickerList.collectAsState(initial= emptyList()).value.toSnapshotStateList()
        memoModel.stickerList.collect{it ->
            stickerViewModel.selectedArray.addAll(it)
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
                    DrawerContent(navController)
                }
            )
        },
        scrimColor = Color.Black.copy(alpha = 0.32f)
    ) {
        Scaffold(
            topBar = {
                title?.let {
                    TopBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        kakaoAuthViewModel,
                        pageId,
                        it,
                        navController
                    )
                }
            },
            floatingActionButton = {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
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
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Image(
                    painter = themeBackground,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val listState = rememberLazyListState()
                    val firstVisibleItemIndex = listState.firstVisibleItemIndex
                    val firstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
                    val scrollDp =
                        with(LocalDensity.current) { (firstVisibleItemIndex * 180).dp + firstVisibleItemScrollOffset.toDp() }
                    var imageOffset by remember { mutableStateOf(Offset.Zero) }

                    Box {
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
                                        if (pageId != null) {
                                            MemoItem(
                                                MemoContents = item,
                                                modifier = Modifier.weight(1f),
                                                memoModel,
                                                pageId
                                            )
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
//                                painter = painterResource(id = R.drawable.ku10),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(130.dp)
                                    .padding(top = 15.dp)

                            )
                            if (sticker.deletable) {
                                IconButton(
                                    onClick = {
                                        val stId = stickerViewModel.selectedArray[idx].id
                                        memoModel.deleteSticker(pageId!!, stId)
                                        stickerViewModel.removeSticker(idx)
                                    },
                                    modifier = Modifier
                                        .size(130.dp)
                                        .padding(top = 15.dp)
                                )
                                if (sticker.deletable) {
                                    IconButton(
                                        onClick = {
                                            stickerViewModel.removeSticker(idx)
                                        },
                                        modifier = Modifier.align(Alignment.TopEnd)
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
                    }
                    MainScreen(memoModel = memoModel, pageId = pageId!!)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onMenuClick: () -> Unit,
    viewModel: KakaoAuthViewModel,
    pageId: String?,
    title: String,
    navController: NavController
) {
    TopAppBar(
        title = { Text("제목: $title", fontSize = 20.sp) },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("SharePage/${pageId}")
            }) {
                Icon(
                    imageVector = Icons.Default.Share,
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
fun DrawerContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "홈",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    navController.navigate(Routes.Home.route)
                }
        )

        Text(
            text = "팀원 소개",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    navController.navigate(Routes.TeamPage.route)
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoItem(
    MemoContents: Memo,
    modifier: Modifier = Modifier,
    memoModel: MemoViewModel,
    pageId: String
) {
    val rotationAngle by remember { mutableStateOf(Random.nextFloat() * 10 - 5) } // -5도에서 5도 사이의 랜덤 각도
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
                colors = CardDefaults.cardColors(
                    containerColor = Colors.getColorByIndex(
                        MemoContents.memoColor
                    )
                ) // memoColor를 배경색으로 지정
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = MemoContents.content,
                        color = FontColors.getFontColorByIndex(MemoContents.fontColor),
                        fontFamily = Fonts.getFontByIndex(MemoContents.font),
                        fontSize = MemoContents.fontSize.sp
                    )
                    Text(
                        text = MemoContents.name,
                        color = FontColors.getFontColorByIndex(MemoContents.fontColor),
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
                    modifier = Modifier.clickable {
                        likes++
                        memoModel.increaseLike(pageId, MemoContents.memoId)
                    }
                )
            }
        }
    }
}
