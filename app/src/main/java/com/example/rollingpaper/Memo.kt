package com.example.rollingpaper

data class Memo(var memoId:Int,var content:String, var name: String, var font:Int,var fontSize:Int,var fontColor:Int,var memoColor:Int, var like:Int,var pageId:Int){
    constructor() : this(0, "", "", 0, 0, 0, 0, 0, 0)
}
