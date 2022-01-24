package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ContentDetailBinding
import com.udacity.utils.DownloadStatus
import com.udacity.utils.INTENT_DOWNLOAD_REPO
import com.udacity.utils.INTENT_DOWNLOAD_STATUS
import com.udacity.utils.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val downloadedRepo = intent.getStringExtra(INTENT_DOWNLOAD_REPO)
        binding.contentDetail.downloadFileName.text = downloadedRepo
        val stat = intent.getStringExtra(INTENT_DOWNLOAD_STATUS)
        binding.contentDetail.status = stat

        binding.contentDetail.closeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelNotifications()
    }

}
