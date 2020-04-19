package com.example.halodoc.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast

abstract class BaseActivity : AppCompatActivity() {

    private var backPressedOnce = false

    companion object {
        //Define your companion objects whose value will persist across the activities
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Set Light Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun extractData()
    abstract fun initComponents()
    abstract fun navigateBack()

    override fun onBackPressed() {
        navigateBack()
        estimateDoubleBackPress()
    }

    private fun estimateDoubleBackPress() {
        if (backPressedOnce) finish()
        else
        {
            this.backPressedOnce = true
            Toast.makeText(this@BaseActivity, "Press again to Exit!", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ backPressedOnce = false }, 2000)
        }
    }

}