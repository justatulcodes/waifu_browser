package com.example.waifubrowserv21.activities

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Entity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.example.waifubrowserv21.utils.Constants
import com.example.waifubrowserv21.R
import com.example.waifubrowserv21.databinding.ActivityWaifuViewBinding
import com.example.waifubrowserv21.utils.AppFunctions
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.URL

class WaifuViewActivity : BaseActivity() {
    lateinit var binding : ActivityWaifuViewBinding
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaifuViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loadingText.text = "Please wait..."
        val imageUrl = intent.getStringExtra(Constants.IMAGE_URL)
        if(intent.hasExtra(Constants.IMAGE_URL)){
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.loading)
                .into(binding.waifuView)
            binding.loadingText.text = ""

        }else{
            binding.loadingText.text = "No image available,please restart."
        }

        binding.downloadButton.setOnClickListener {
            if(AppFunctions().isInternetAvailable(this)){
                saveFileUsingMediaStore(this,
                    intent.getStringExtra(Constants.IMAGE_URL)!!,
                    "${intent.getStringExtra(Constants.IMAGE_ID)}.jpg")
            }else{
                showSnackBarMessage("No internet connection available.")
            }

        }
        binding.setWallpaperButton.setOnClickListener {
            showProgressDialog()
            setWallpaper()
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveFileUsingMediaStore(context: Context, url: String, fileName: String) {

        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle("Waifu Image")
        request.setDescription("Waifu browser download.")
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setMimeType("image/jpg")

        val manager = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        showSnackBarMessage("Saving waifu in gallery...")
    }
    

    private fun setWallpaper() {
        val wallpaperManager : WallpaperManager = WallpaperManager.getInstance(this)
        try {
            Thread {
                val drawable = binding.waifuView.drawable
                val bitmap = drawable.toBitmap()
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                runOnUiThread {
                    showSnackBarMessage("Wallpaper set successfully!")
                    hideProgressDialog()
                }
            }.start()

        } catch (e: IOException) {
            try {
                wallpaperManager.setBitmap(binding.waifuView.drawable.toBitmap())
            } catch (f: IOException) {
                f.printStackTrace()
            }
            e.printStackTrace()
        }

    }


}