package com.example.josequaltask.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.josequaltask.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val context: Context, private val imageUrl:Bitmap?) : GoogleMap.InfoWindowAdapter {
    val view: View = LayoutInflater.from(context).inflate(R.layout.item_marker_title, null)
    override fun getInfoWindow(marker: Marker): View {

        val imageView = view.findViewById<ImageView>(R.id.user_image)
        val textView = view.findViewById<TextView>(R.id.titel)

        textView.text = marker.title
        imageView.setImageBitmap(imageUrl)

        return view
    }

    override fun getInfoContents(marker: Marker): View? {

        return null
    }
}
