package com.example.instagram.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagram.DeleteVideoImage
import com.example.instagram.ProgressDialog
import com.example.instagram.R
import com.example.instagram.adapter.ProfilePhotoAdapter
import com.example.instagram.interfac.PersonalVideo
import com.example.instagram.model.UploadVideo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage

class ProfilePhotosFragment() : Fragment(), PersonalVideo{

    private lateinit var photos: ArrayList<UploadVideo>
    private lateinit var adapter: ProfilePhotoAdapter
    private lateinit var recyclerView: RecyclerView
//    private lateinit var frameLayout: FrameLayout

    private val auth = FirebaseAuth.getInstance().currentUser?.uid
    private var firebaseStore = Firebase.firestore

    private lateinit var editImage: ImageView
    private lateinit var editBio: EditText
    private lateinit var saveBtn: AppCompatButton
    private lateinit var constraintLayout: ConstraintLayout

    private lateinit var imageUri: Uri

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_photos, container, false)
//        frameLayout = view.findViewById(R.id.frameLayout)
        editImage = view.findViewById(R.id.editImage)
        editBio = view.findViewById(R.id.editImageDesc)
        saveBtn = view.findViewById(R.id.saveImageBtn)
        constraintLayout = view.findViewById(R.id.editImagePage)

        recyclerView = view.findViewById(R.id.recyclerPhotosP)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 4)
        photos = arrayListOf()

        getData()

        adapter = ProfilePhotoAdapter(requireActivity(),photos, this)
        recyclerView.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    private fun getData(){
        val collection = firebaseStore.collection(auth.toString())
        collection.document("media")
            .collection("all_images")
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty){

                    for (item in result.documents){

                        val id = item.getString("id")
                        val username = item.getString("username")
                        val description = item.getString("description")
                        val imageUri = item.getString("videoUri")
                        val imageName = item.getString("videoImageName")
                        val time = item.getString("time")

                        photos.add(UploadVideo(id, username, description, imageUri, imageName, time))
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun postDeleteFun(contentName: String, contentId: String) {

        val delete = DeleteVideoImage(requireActivity(), "all_images")
        delete.deleteContentFun(contentName, contentId)
    }

    override fun postEditFun(contentName: String, postUri: String, desc: String, username: String, time: String, id: String) {
        recyclerView.visibility = View.GONE
//        frameLayout.visibility = View.VISIBLE
//        loadFragment(EditPostFragment())
        constraintLayout.visibility = View.VISIBLE

        Glide.with(requireActivity()).load(postUri).into(editImage)
        editBio.setText(desc)

        editImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 201)
        }
        saveBtn.setOnClickListener {
            updateImage(contentName, imageUri ,editBio.text.toString(), username, time, id)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 201 && resultCode == Activity.RESULT_OK){
            if (data != null && data.data != null){
                imageUri = data.data!!
                editImage.setImageURI(imageUri)
            }
        }
    }

    private fun updateImage(imageName: String, imageUrl: Uri, bio: String, username: String, time: String, id: String){
        val progress = ProgressDialog(requireActivity())
        progress.dialogShow("Update image", "Updating your image please wait...")
        val firesStorage = FirebaseStorage.getInstance()
        val storageRef = firesStorage.getReference("all_images")
        storageRef.child(imageName)
            .putFile(imageUrl)
            .addOnSuccessListener { task ->
                task.storage.downloadUrl.addOnSuccessListener { uri ->
                    //fireStore
                    val mapArray = mapOf("description" to bio, "id" to id, "time" to time, "username" to username, "videoImageName" to imageName, "videoUri" to uri.toString())
                    val fireStore = FirebaseFirestore.getInstance()
                    fireStore.collection(auth.toString())
                        .document("media")
                        .collection("all_images")
                        .document(id)
                        .update(mapArray)
                        .addOnFailureListener {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                            progress.progressDialog.dismiss()
                        }
                    //realtime database
                    val realTimeDb = FirebaseDatabase.getInstance()
                    val realTimeDbRef = realTimeDb.getReference("all_images")
                    realTimeDbRef.child(id)
                        .setValue(UploadVideo(id, username, bio, uri.toString(), imageName, time))
                        .addOnFailureListener {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                            progress.progressDialog.dismiss()
                        }
                }
                progress.progressDialog.dismiss()
            }
        recyclerView.visibility = View.VISIBLE
        constraintLayout.visibility = View.GONE
        adapter.notifyDataSetChanged()
    }
//    fun loadFragment(fragment: Fragment){
//        val transition = fragmentManager?.beginTransaction()
//        transition?.replace(R.id.frameLayout, fragment)
//        transition?.addToBackStack(null) // Optional: Adds the transaction to the back stack
//        transition?.commit()
//    }
}