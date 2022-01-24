package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.udacity.utils.DownloadStatus
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url: String? = null
    private lateinit var repoTitle: String
    private lateinit var repoDescription: String
    private lateinit var notificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadButtonListener()

        createChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_name)
        )
    }

    private fun downloadButtonListener () {
        custom_button.setOnClickListener {
            val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
            val selectedRadio  = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
            repoTitle = selectedRadio.text.toString()
            when(selectedRadio?.text){
                getString(R.string.notification_title_glide) -> {
                    url = "${PREFIX_URL}/bumptech/glide"
                    repoDescription = getString(R.string.notification_description_glide)
                }
                getString(R.string.notification_title) -> {
                    url ="${PREFIX_URL}/udacity/nd940-c3-advanced-android-programming-project-starter${SUFFIX_URL}"
                    repoDescription = getString(R.string.notification_description)
                }
                getString(R.string.notification_title_retrofit) -> {
                    url = "${PREFIX_URL}/square/retrofit.git"
                    repoDescription = getString(R.string.notification_description_retrofit)
                }
                else -> {
                    url = null
                    repoDescription = ""
                }
            }
            if (url != null){
                custom_button.setButtonState(ButtonState.Loading)
                download()
            } else {
                Toast.makeText(this, getString(R.string.invalid_download), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun download() {
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

    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                setShowBadge(false)
                enableVibration(true)
                description = getString(R.string.channel_description)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var status: DownloadStatus = DownloadStatus.Unavailable
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(id!!))

            if (cursor.moveToNext()) {
                val currStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                cursor.close()
                status = if (currStatus == DownloadManager.STATUS_SUCCESSFUL) DownloadStatus.Success else DownloadStatus.Failed
            }
            notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager.sendNotification(
                status,
                repoTitle,
                repoDescription,
                applicationContext
            )
        }
    }

    companion object {
        private const val PREFIX_URL = "https://github.com"
        private const val SUFFIX_URL = "/archive/master.zip"
    }

}
