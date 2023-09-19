package com.example.instagram.interfac

    // use = ProfilePhotosFragment , ProfileVideoFragment
interface PersonalVideo {

        //contentName(videoImageName) and contentId get from fireStore database
    fun postDeleteFun(contentName: String, contentId: String)
    fun postEditFun(contentName: String, postUri: String, desc: String, username: String, time: String, id: String)
}