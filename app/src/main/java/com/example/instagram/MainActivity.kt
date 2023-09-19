package com.example.instagram

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.instagram.fragments.AccountFragment
import com.example.instagram.fragments.HomeFragment
import com.example.instagram.fragments.Post_PhotosVideosFragment
import com.example.instagram.fragments.ReelsFragment
import com.example.instagram.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private var storage = FirebaseStorage.getInstance()

    private var fireStoreDatabase = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val footerLayout : BottomNavigationView = findViewById(R.id.footerLayout)

        loadFragment(HomeFragment())
        footerLayout.setOnItemSelectedListener {
            when(it.itemId){

                R.id.home -> loadFragment(HomeFragment())
                R.id.reels -> loadFragment(ReelsFragment())
                R.id.account -> loadFragment(AccountFragment())
                R.id.search -> loadFragment(SearchFragment())
                R.id.newPost -> {
                    loadFragment(Post_PhotosVideosFragment())
//                    openBottomSheetForPost()
                }
            }
            true
        }

    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    private fun openBottomSheetForPost(){

        val layout = layoutInflater.inflate(R.layout.post_bottom_sheet, null, false)
        val photosBtn: ImageButton = layout.findViewById(R.id.pickPhotosBtn)
        val videosBtn: ImageButton = layout.findViewById(R.id.pickVideoBtn)
        val cameraBtn: ImageButton = layout.findViewById(R.id.openCameraBtn)

        val bottomSheet = BottomSheetDialog(this)
        bottomSheet.setContentView(layout)
        bottomSheet.show()
    }

}
