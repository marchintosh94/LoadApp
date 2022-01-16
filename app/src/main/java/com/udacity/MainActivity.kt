package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadButtonListener()
    }

    private fun downloadButtonListener () {
        custom_button.setOnClickListener {
            val selectedRadio  = findViewById<RadioButton>(radio_group.checkedRadioButtonId)
            val url = when(selectedRadio.text){
                getString(R.string.glide_repo) -> "${PREFIX_URL}/bumptech/glide${SUFFIX_URL}"
                getString(R.string.project_repo) -> "${PREFIX_URL}/udacity/nd940-c3-advanced-android-programming-project-starter${SUFFIX_URL}"
                getString(R.string.retrofit_repo) -> "${PREFIX_URL}/square/retrofit${SUFFIX_URL}"
                else -> return@setOnClickListener
            }
            val fullUrl = "${PREFIX_URL}${url}${SUFFIX_URL}"
            download(fullUrl)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val PREFIX_URL = "https://github.com"
        private const val SUFFIX_URL = "/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
