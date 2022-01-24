package com.udacity.utils

const val INTENT_DOWNLOAD_REPO = "com.udacyty.LoadApp.DownloadRepo"
const val INTENT_DOWNLOAD_STATUS = "com.udacyty.LoadApp.Status"

enum class DownloadStatus {
    Success,
    Failed,
    Unavailable
}