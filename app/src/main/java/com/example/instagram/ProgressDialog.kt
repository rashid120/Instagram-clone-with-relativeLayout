package com.example.instagram

import android.app.ProgressDialog
import android.content.Context

class ProgressDialog(context: Context) {

    val progressDialog = ProgressDialog(context)

    fun dialogShow(tittle: String, message: String)
    {
        progressDialog.setTitle(tittle)
        progressDialog.setMessage(message)
        progressDialog.show()
    }
}