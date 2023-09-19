package com.example.instagram.model

// front page ke liye hai
data class UserPostData(

    val shorProfileIcon : Int? = null,
    val username : String? = null,
    val userPostImage : Int? = null,

    //fake likes, description, comments and times
    val showLikes : String? = null,
    val showDescription : String? = null,
    val showFixedComment : String? = null,
    val showPostTime : String? = null

)
