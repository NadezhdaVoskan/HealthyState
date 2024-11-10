package com.example.healthystate.Navigations

import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.healthystate.R

class DetailsRecommend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_details_recommend)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        val titleTextView: TextView = findViewById(R.id.title_text_view)
        val previewTextView: TextView = findViewById(R.id.preview_text_view)
        val imageTitleView: ImageView = findViewById(R.id.image_view)

        val image = intent.getIntExtra("image",0)
        val title = intent.getStringExtra("title")
        val preview = intent.getStringExtra("preview")


        titleTextView.text = title
        previewTextView.text = preview
        imageTitleView.setImageResource(image)
    }
}