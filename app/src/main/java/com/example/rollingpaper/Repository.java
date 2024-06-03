package com.example.rollingpaper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

class Repository(private val tabe:DatabaseReference) {


    suspend fun insertMemo(memo: Memo){
        table.child(memo.memoId.toString()).setValue(memo)
    }


}
