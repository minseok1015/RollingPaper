package com.example.rollingpaper

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class Repository(private val table: DatabaseReference) {
    suspend fun insertMemo(pageId: String, memo: Memo,currentMemoId:Int) {
        table.child(pageId).child("memos").child(memo.memoId.toString()).setValue(memo)
        table.child("currentMemoId").setValue(currentMemoId + 1)
    }

    suspend fun insertSticker(pageId: String, stickerId: String, x: Float, y: Float) {
        table.child("${pageId}/sticker/${stickerId}/x").setValue(x)
        table.child("${pageId}/sticker/${stickerId}/y").setValue(y)
    }

    suspend fun deleteSticker(pageId: String, stickerId: String) {
        table.child("${pageId}/sticker/${stickerId}").removeValue()
    }

    fun getAllStickers(pageId: String, context: Context): Flow<List<SelectedSticker>> =
        callbackFlow {

            val stListener2 = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val stli = mutableListOf<SelectedSticker>()
                    for (itemSnpashot in snapshot.children) {
                        var imgIdx = itemSnpashot.getKey().toString().split("X")[0]

                        val resourceId = context.resources.getIdentifier(
                            "ku${imgIdx}",
                            "drawable",
                            context.packageName
                        )

//                        var x = itemSnpashot.child("x").getValue().toString().toFloat()
                        var x = itemSnpashot.child("x").getValue()
                        var y = itemSnpashot.child("y").getValue()
                        var fx: Float = 0.0f
                        var fy: Float = 0.0f
                        if (x != null && y != null) {
                            fx = x.toString().toFloat()
                            fy = y.toString().toFloat()
                        }

                        var id = itemSnpashot.getKey().toString()
                        stli.add(
                            SelectedSticker(
                                sticker = ContextCompat.getDrawable(context, resourceId),
                                id = id,
                                offsetX = fx,
                                offsetY = fy,
                            )
                        )
                    }
                    trySend(stli)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

//            val stListner = object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val stli = mutableListOf<SelectedSticker>()
//                    for (itemSnpashot in snapshot.children) {
//                        Log.d("mymyss", snapshot.children.toString())
//                        var imgIdx = itemSnpashot.getKey().toString().split("X")[0]
//
//                        val resourceId = context.resources.getIdentifier(
//                            "ku${imgIdx}",
//                            "drawable",
//                            context.packageName
//                        )
//
////                        var x = itemSnpashot.child("x").getValue().toString().toFloat()
//                        var x = itemSnpashot.child("x").getValue()
//                        var y = itemSnpashot.child("y").getValue()
//                        var fx: Float = 0.0f
//                        var fy: Float = 0.0f
//                        if (x != null && y != null) {
//                            fx = x.toString().toFloat()
//                            fy = y.toString().toFloat()
//                        }
//
//                        var id = itemSnpashot.getKey().toString()
//                        stli.add(
//                            SelectedSticker(
//                                sticker = ContextCompat.getDrawable(context, resourceId),
//                                id = id,
//                                offsetX = fx,
//                                offsetY = fy,
//                            )
//                        )
//                    }
//                    trySend(stli)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            }
//            table.child("${pageId}/sticker").addValueEventListener(stListner)
//            awaitClose {
//                table.child("${pageId}/sticker").removeEventListener(stListner)
//            }
            table.child("${pageId}/sticker").addChildEventListener(stListener2)
            awaitClose {
                table.child("${pageId}/sticker").removeEventListener(stListener2)
            }
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

    suspend fun increaseLike(pageId: String, memoId: Int) {
        val memoRef = table.child(pageId).child("memos").child(memoId.toString())
        memoRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val currentLikes = currentData.child("like").getValue(Int::class.java) ?: 0
                currentData.child("like").value = currentLikes + 1
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    throw DatabaseException(error.message)
                }
            }
        })
    }
    suspend fun getCurrentMemoId(): Int {
        val dataSnapshot = table.child("currentMemoId").get().await()
        return dataSnapshot.getValue(Int::class.java) ?: 0
    }

}