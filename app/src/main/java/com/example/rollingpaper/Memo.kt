package com.example.rollingpaper

import androidx.compose.ui.graphics.Color

data class Memo(var memoId:Int,var content:String, var name: String, var font:String,var fontSize:Int,var fontColor:Color,var memoColor:Color, var like:Int,var pageId:Int)
