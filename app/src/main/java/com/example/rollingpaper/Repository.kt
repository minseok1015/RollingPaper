package com.example.rollingpaper

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class Repository(private val table: DatabaseReference) {
    suspend fun insertMemo(memo: Memo){
        table.child(memo.memoId.toString()).setValue(memo)
    }


    suspend fun insertPage(page: Page) {
        table.child(page.pageId).setValue(page)
    }

    fun getAllMemos(): Flow<List<Memo>> = callbackFlow{
        val listner= object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val memoList = mutableListOf<Memo>()
                for(itemSnpashot in snapshot.children){
                    val memo=itemSnpashot.getValue(Memo::class.java)
                    memo?.let { memoList.add(memo) }
                }
                trySend(memoList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        table.addValueEventListener(listner)
        awaitClose{
            table.removeEventListener(listner)
        }
    }

}