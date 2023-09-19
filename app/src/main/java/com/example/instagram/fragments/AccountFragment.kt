package com.example.instagram.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.example.instagram.LoginActivity
import com.example.instagram.R
import com.example.instagram.adapter.ViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : androidx.fragment.app.Fragment() {

    private val auth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val localStorage = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)

        val tabLayout = view.findViewById<TabLayout>(R.id.photosVideosContactsTabLayout)
        val viewPager = view.findViewById<ViewPager>(R.id.viewPager)

        // ViewPagerAdapter class instant/object
        val viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)

        //add fragment
        viewPagerAdapter.fragments.add(ProfilePhotosFragment())
        viewPagerAdapter.fragments.add(ProfileVideoFragment())
        viewPagerAdapter.fragments.add(ProfileContactsFragment())

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        // set icon on TabLayout
        tabLayout.getTabAt(0)?.setIcon(R.drawable.account_post)
//        tabLayout.getTabAt(1)?.setText("Post")
        tabLayout.getTabAt(1)?.setIcon(R.drawable.account_reels)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.contacts)

        //Profile BottomSheet
        val toolBar:androidx.appcompat.widget.Toolbar = view.findViewById(R.id.profileToolbar)

        toolBar.setOnMenuItemClickListener {
            when(it.itemId){

                R.id.create -> profileBottomSheet()
            }
            true
        }

        // Inflate the layout for this fragment
        return view
    }


    @SuppressLint("MissingInflatedId")
    private fun profileBottomSheet() {

        val layout = layoutInflater.inflate(R.layout.activity_profile_drawer, null)
        val logOut = layout.findViewById<TextView>(R.id.logOut)
        val delete = layout.findViewById<TextView>(R.id.deleteMyId)

        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetDialog.setContentView(layout)

        // for logOut
        logOut.setOnClickListener {

            auth.signOut()
            requireActivity().startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        //delete Account
        delete.setOnClickListener {
            auth.currentUser?.delete()
        }

        bottomSheetDialog.show()
    }

}