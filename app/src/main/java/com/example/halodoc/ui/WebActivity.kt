package com.example.halodoc.ui

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.halodoc.R
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initComponents()
    }

    private fun initComponents(){
        val url = intent.extras?.getString("URL")
        url?.run {
            val webSettings = webview.settings
            webSettings.javaScriptEnabled = true
            webview.webViewClient = WebViewClient()
            webview.loadUrl(this )
        }
    }
}
