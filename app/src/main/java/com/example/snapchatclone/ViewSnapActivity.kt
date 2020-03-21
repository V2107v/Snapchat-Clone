package com.example.snapchatclone

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    var messageTextView : TextView? = null
    var snapImageView : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        snapImageView = findViewById(R.id.snapImageView)
        messageTextView = findViewById(R.id.messageTextView)

        messageTextView?.text = intent.getStringExtra("message")

        val task = ImageDownloader()
        val myImage: Bitmap
        try{
            myImage = task.execute(intent.getStringExtra("imageUrl")).get()
            snapImageView?.setImageBitmap(myImage)
        } catch (e:java.lang.Exception) {
            e.printStackTrace()
        }

    }

    inner class ImageDownloader : AsyncTask<String,Void, Bitmap> () {
        override fun doInBackground(vararg urls: String): Bitmap? {
            try{
                val url = URL(urls[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val `in` = connection.inputStream
                return BitmapFactory.decodeStream(`in`)
             } catch (e: Exception) {
                e.printStackTrace()
                return  null
            }
        }

    }
}
