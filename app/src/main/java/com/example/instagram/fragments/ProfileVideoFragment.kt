package com.example.instagram.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.DeleteVideoImage
import com.example.instagram.R
import com.example.instagram.adapter.PersonalVideoAdapter
import com.example.instagram.interfac.PersonalVideo
import com.example.instagram.model.UploadVideo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileVideoFragment : Fragment(),PersonalVideo {

    private lateinit var myVideosItem: ArrayList<UploadVideo>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonalVideoAdapter

    private var fireStore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var media: ArrayList<UploadVideo> = arrayListOf()
    private var contentName2: String? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_video, container, false)

        val noVideoText: TextView = view.findViewById(R.id.no_video)
        recyclerView = view.findViewById(R.id.recyclerViewProfileVideo)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

        myVideosItem = arrayListOf()
        getData()
        adapter = PersonalVideoAdapter(requireActivity(), myVideosItem, this)
        recyclerView.adapter = adapter
        
/*
        if (media.isEmpty()){
            noVideoText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

        }else{
            noVideoText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }*/

        // Inflate the layout for this fragment
        return view
    }
    
    private fun getData(){

        fireStore.collection(auth.currentUser?.uid.toString())
            .document("media")
            .collection("all_videos")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result

                    if (querySnapshot != null && !querySnapshot.isEmpty) {

                        for (document in querySnapshot.documents) {

                            val id = document.getString("id")
                            val username = document.getString("username")
                            val description = document.getString("description")
                            val videoUri = document.getString("videoUri")!!
                            val videoImageName = document.getString("videoImageName")
                            val time = document.getString("time")

                            // Add all items to the arrayList
                            myVideosItem.add(UploadVideo(id, username, description, videoUri, videoImageName, time))
                            adapter.notifyDataSetChanged()
                        }
//                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(requireActivity(), "No videos found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle the error
                    Toast.makeText(requireActivity(), "Error loading videos", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun postDeleteFun(contentName: String, contentId: String) {

        val deleteC = DeleteVideoImage(requireActivity(), "all_videos")
        deleteC.deleteContentFun(contentName, contentId)
        Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show()
        Toast.makeText(requireActivity(), "refresh app", Toast.LENGTH_SHORT).show()
    }

    override fun postEditFun(contentName: String, postUri: String, desc: String, username: String, time: String, id: String) {

        contentName2 = contentName
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "video/*"
        startActivityForResult(intent, 313)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 313 ){
            if (data != null && data.data != null){
                val videoUri = data.data
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.getReference("all_videos")
                storageRef.child(contentName2!!)
                    .putFile(videoUri!!)
                    .addOnSuccessListener {
                        Toast.makeText(requireActivity(), "Uploaded", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}