package com.example.instagram

import android.content.Context
import android.widget.Toast
import com.example.instagram.model.UploadVideo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GetVideosRealTimeDB(val context: Context) {

    private val database = FirebaseDatabase.getInstance()
    var dataItems: ArrayList<UploadVideo> = arrayListOf()

    fun getData(): ArrayList<UploadVideo>{

        val reference = database.getReference("all_videos")
        reference.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                Toast.makeText(context, "find successfully", Toast.LENGTH_SHORT).show()
                for (childSnapshot in snapshot.children){
                    val videoData = childSnapshot.getValue(UploadVideo::class.java)
                    if (videoData != null) {
                        dataItems.add(videoData)
                    }
                    Toast.makeText(context, dataItems[0].videoUri, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
        return dataItems
    }
}