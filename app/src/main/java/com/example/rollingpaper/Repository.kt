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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class Repository(private val table: DatabaseReference) {
    suspend fun insertMemo(memo: Memo) {
        table.child(memo.memoId.toString()).setValue(memo)
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

    fun getAllMemos(): Flow<List<Memo>> = callbackFlow {
        val listner = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val memoList = mutableListOf<Memo>()
                for (itemSnpashot in snapshot.children) {
                    val memo = itemSnpashot.getValue(Memo::class.java)
                    memo?.let { memoList.add(memo) }
                }
                trySend(memoList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        table.addValueEventListener(listner)
        awaitClose {
            table.removeEventListener(listner)
        }
    }

}