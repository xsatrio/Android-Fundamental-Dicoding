package com.dicoding.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuser.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splash = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)

                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        splash.start()
    }
}
