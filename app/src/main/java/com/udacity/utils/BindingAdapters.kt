package com.udacity.utils

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("statusText")
fun bindStatusText(view: TextView, status: String){
    when(status){
        "Failed" -> {
            view.text = DownloadStatus.Failed.toString()
            view.setTextColor(Color.RED)
        }
        "Success" -> {
            view.text = DownloadStatus.Success.toString()
            view.setTextColor(Color.GREEN)
        }
        else -> {
            view.text = DownloadStatus.Unavailable.toString()
            view.setTextColor(Color.GRAY)
        }
    }
}