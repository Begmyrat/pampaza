package com.fabrika.pampaza.common.ui

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.fabrika.pampaza.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("pampaza:longToDateString")
fun TextView.convertLongToDateString(date: Long?) {
    date?.let {
        text = SimpleDateFormat("dd/MM/yyyy", Locale("en")).format(Date(date))
    }
}

@BindingAdapter("pampaza:loadImageUrl")
fun ImageView.loadImageUrl(url: String?){
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    url?.let {
        Glide.with(context).load(it).placeholder(circularProgressDrawable).into(this)
    }
}