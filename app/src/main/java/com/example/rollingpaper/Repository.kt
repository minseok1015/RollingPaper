package com.example.rollingpaper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class Repository(private val table: DatabaseReference) {
    suspend fun insertMemo(pageId: String, memo: Memo) {
        table.child(pageId).child("memos").child(memo.memoId.toString()).setValue(memo)
    }


    suspend fun insertPage(page: Page) {
        table.child(page.pageId).setValue(page)
    }

    fun getAllMemos(pageId: String): Flow<List<Memo>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val memoList = mutableListOf<Memo>()
                for (itemSnapshot in snapshot.children) {
                    val memo = itemSnapshot.getValue(Memo::class.java)
                    memo?.let { memoList.add(memo) }
                }
                trySend(memoList)
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        }
        table.child(pageId).child("memos").addValueEventListener(listener)
        awaitClose {
            table.child(pageId).child("memos").removeEventListener(listener)
        }
    }

    fun getPageInfo(pageId: String): Flow<Page?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val page = snapshot.getValue(Page::class.java)
                trySend(page)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        table.child(pageId).addListenerForSingleValueEvent(listener)
        awaitClose { table.child(pageId).removeEventListener(listener) }
    }

}