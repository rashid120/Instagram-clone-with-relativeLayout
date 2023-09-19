package com.example.instagram.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.instagram.GetVideosRealTimeDB
import com.example.instagram.R
import com.example.instagram.adapter.ReelsAdapter
import com.example.instagram.model.ReelsModel
import com.example.instagram.model.UploadVideo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ReelsFragment : Fragment() {

    private lateinit var reelsItem : ArrayList<UploadVideo>
    private lateinit var adapter : ReelsAdapter
    private val auth = FirebaseAuth.getInstance().currentUser?.uid
    private var storage = FirebaseStorage.getInstance()
    private lateinit var viewPager2: ViewPager2

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reels, container, false)

        viewPager2 = view.findViewById(R.id.reelsViewPager)

        reelsItem = arrayListOf()
        // call function
        getVideo()
        // create object of GetVideosRealTimeDatabase class
//        val database = GetVideosRealTimeDB(requireActivity())
        //get data from database and set in to reelsItem array
//        reelsItem = database.getData()

        adapter = ReelsAdapter(requireActivity(), reelsItem)
        viewPager2.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    private fun getVideo(){
        //get data from realtime database
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("all_videos")
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                Toast.makeText(context, "find successfully", Toast.LENGTH_SHORT).show()
                for(childSnapshot in snapshot.children){
                    val videoData = childSnapshot.getValue(UploadVideo::class.java)
                    if (videoData != null) {
                        reelsItem.add(videoData)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

}