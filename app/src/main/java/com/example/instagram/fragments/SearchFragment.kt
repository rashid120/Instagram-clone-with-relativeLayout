package com.example.instagram.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.instagram.R

class SearchFragment : Fragment() {

    private lateinit var webView: WebView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        webView = view.findViewById(R.id.webView)
//        webView.loadUrl("https://www.youtube.com/shorts/lg30egX4-LY")
        webView.loadUrl("https://www.google.com")
//        webView.loadUrl("https://www.youtube.com")

        webView.webViewClient = MyWeb()

        // Inflate the layout for this fragment
        return view
    }

//    fun onBackPressed() {
//
//        if (webView.canGoBack()){
//
//            webView.goBack()
//        }else{
//            requireActivity().onBackPressed()
//        }
//    }

    class MyWeb : WebViewClient(){
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {

            view?.loadUrl(request?.url.toString())
            return true
        }

        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

            view?.loadUrl(url.toString())
            return true
        }
    }
}