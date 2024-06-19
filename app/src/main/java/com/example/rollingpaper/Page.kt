package com.example.rollingpaper

data class Page(var pageId:String,var theme:Int,var title:String){
    constructor() : this("", 0, "")
}


