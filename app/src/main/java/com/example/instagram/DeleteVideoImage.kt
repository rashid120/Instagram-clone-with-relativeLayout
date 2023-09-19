package com.example.instagram

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class DeleteVideoImage(val context: Context, private val reference: String) {

    //reference = all_videos or all_images

    // storage
    private val storage = FirebaseStorage.getInstance()
    private val storageReference = storage.getReference(reference)

    // fireStore Database
    private val auth = FirebaseAuth.getInstance().currentUser?.uid
    private val fireStore = FirebaseFirestore.getInstance()
    private val storeRef = fireStore.collection(auth.toString())

    // realtime database
    private val realTimeDB = FirebaseDatabase.getInstance()
    private val realTimeDBRef = realTimeDB.getReference(reference)

    fun deleteContentFun(contentName: String, contentId: String){

        //contentName(videoImageName) and contentId get from fireStore database
        //delete from storage
        storageReference.child(contentName)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "delete from Storage", Toast.LENGTH_SHORT).show()
            }

        // delete from fireStore database
        storeRef.document("media").collection(reference).document(contentId).delete()
            .addOnSuccessListener {
                Toast.makeText(context, "delete from store", Toast.LENGTH_SHORT).show()
            }

        // delete from realtime database
        realTimeDBRef.child(contentId).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "deleted from realtime database", Toast.LENGTH_SHORT).show()
            }
    }
}