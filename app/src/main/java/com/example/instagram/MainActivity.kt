package com.example.instagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "App is running", Toast.LENGTH_SHORT).show()

        val heart = findViewById<ImageView>(R.id.insta) as ImageView
        heart.setOnClickListener{
            Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
//        val heart = findViewById<ImageView>(R.id.insta) as ImageView

    }

}
