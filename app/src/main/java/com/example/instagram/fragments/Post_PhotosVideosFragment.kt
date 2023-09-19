package com.example.instagram.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.widget.AppCompatButton
import com.example.instagram.GetCurrentDate
import com.example.instagram.ProgressDialog
import com.example.instagram.R
import com.example.instagram.model.UploadVideo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class Post_PhotosVideosFragment : androidx.fragment.app.Fragment() {

    private lateinit var videoView: VideoView
    private lateinit var imageView: ImageView
    private lateinit var postBtn: AppCompatButton
    private lateinit var descEditText: EditText
    private lateinit var usernameEditText: EditText

    private val auth = FirebaseAuth.getInstance()
    private var storage = FirebaseStorage.getInstance()

    private var fireStoreDatabase = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post__photos_videos, container, false)

        videoView = view.findViewById(R.id.videoView1)
        imageView = view.findViewById(R.id.imageView1)
        postBtn = view.findViewById(R.id.postBtn)
        descEditText = view.findViewById(R.id.description)
        usernameEditText = view.findViewById(R.id.userName)

        first()
        // Inflate the layout for this fragment
        return view
    }

    private fun first() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "*/*"
        startActivityForResult(intent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 123 && data?.data != null) {

                val selectedUri: Uri = data.data!!
                // Use content resolver from the context
                val contentResolver = requireActivity().contentResolver
                val mimeType = contentResolver.getType(selectedUri)

                if (mimeType != null) {
                    if (mimeType.startsWith("image")) {
                        // Selected content is a image
                        // Handle image
                        videoView.visibility = View.GONE
                        imageView.visibility = View.VISIBLE
                        imageView.setImageURI(selectedUri)

                        postBtn.setOnClickListener {
                            textValidation()
                            uploadVideoFun(selectedUri, "image", "all_images", "all_images")
                        }

                    } else if (mimeType.startsWith("video")) {
                        // Selected content is a video
                        // Handle video
                        imageView.visibility = View.GONE
                        videoView.visibility = View.VISIBLE
                        val mediaController = MediaController(requireActivity())
                        mediaController.setAnchorView(videoView)
                        videoView.setVideoURI(selectedUri)
                        videoView.setMediaController(mediaController)
                        videoView.start()

                        postBtn.setOnClickListener {
                            textValidation()

                            uploadVideoFun(selectedUri, "video", "all_videos", "all_videos")
                        }
                    }
                }
            }

        }
    }

    private fun uploadVideoFun(selectedUri: Uri, contentType: String, collectionName: String, refInStorage: String) {
        val progDialog = ProgressDialog(requireActivity())
        progDialog.dialogShow(contentType, "")
        storage.getReference(refInStorage)
            .child(auth.currentUser?.uid.toString() + "." + System.currentTimeMillis().toString() + "." + contentType) //video's name.... username ko change karna hai username user ka name hoga
            .putFile(selectedUri)
            .addOnSuccessListener { task ->

                //create progress dialog

                task.storage.downloadUrl.addOnSuccessListener { uri ->

                    val videoImageName = task.storage.name
                    val randomId = UUID.randomUUID().toString()
                    val time = GetCurrentDate().getDateFun()

                    //video's url and name upload in realtime database
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val databaseReference = firebaseDatabase.getReference(collectionName)
                    databaseReference.child(randomId)
                        .setValue(UploadVideo(randomId, usernameEditText.text.toString(), descEditText.text.toString(), uri.toString(), videoImageName, time))

                    // video's details store in fireStoreDatabase
                    fireStoreDatabase.collection(auth.currentUser?.uid.toString())
                        .document("media")
                        .collection(collectionName)
                        .document(randomId)
                        .set(UploadVideo(randomId, usernameEditText.text.toString(), descEditText.text.toString(), uri.toString(), videoImageName, time))
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "uploaded", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                        }
                }
                //dismiss progress dialog
                progDialog.progressDialog.dismiss()

            }
            .addOnFailureListener {

                progDialog.progressDialog.dismiss()
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener {
                val count: Double = 100.0 * it.bytesTransferred / it.totalByteCount
                progDialog.progressDialog.setMessage("Your $contentType is uploading please wait... $count")
            }
    }

    private fun textValidation() {
        if (descEditText.text.isEmpty()) {
            descEditText.error = "Please write something"
            return
        } else if (usernameEditText.text.isEmpty()) {
            usernameEditText.error = "Please username"
            return
        }
    }
}