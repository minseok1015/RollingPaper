package com.example.rollingpaper

import com.google.firebase.database.DatabaseReference

class Repository(private val table: DatabaseReference) {
    suspend fun insertMemo(memo: Memo){
        table.child(memo.memoId.toString()).setValue(memo)
    }

}