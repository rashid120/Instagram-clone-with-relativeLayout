package com.example.instagram.model

    // use on profilePhotoAdapter, PersonalVideoAdapter, ReelsAdapter

data class UploadVideo(

    // use this both image and video upload
    val id: String? = null,
    val username: String? = null,
    val description: String? = null,
    val videoUri: String? = null, // imageUri, from firebase Storage
    val videoImageName: String? = null,
    val time: String? = null
)
