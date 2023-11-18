package com.example.josequaltask.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.example.josequaltask.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import java.io.IOException

object ImageLoad {


    private var infoWindowAdapter: CustomInfoWindowAdapter? = null

//  when Image loaded set it in the image view
    private fun imageTarget(view : ImageView) = object : com.squareup.picasso.Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (bitmap != null) {
                view.setImageBitmap(bitmap)
            }
        }

        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {

        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

    }

//  Load image inside dialog
    fun loadImage(url: String?, image: ImageView) {

        try {
            val target = imageTarget(image)

            Picasso.get()
                .load(url)
                .transform(ImageRoundCorners())
                .placeholder(com.example.josequaltask.R.drawable.ic_launcher_foreground)
                .into(target)

        }catch (e: IOException){
            e
        }


    }







//  when Image loaded set it in the image view
    private fun imageTargetMarker(marker: Marker?, context:Context, mGoogleMap: GoogleMap?, isMarkerClick: Boolean?) = object : com.squareup.picasso.Target {
        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            if (bitmap != null) {
                infoWindowAdapter = context.let { CustomInfoWindowAdapter(it, bitmap) }
                mGoogleMap?.setInfoWindowAdapter(infoWindowAdapter)
                marker?.showInfoWindow()


            }
        }

        override fun onBitmapFailed(e: java.lang.Exception?, errorDrawable: Drawable?) {

        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

        }

    }

//  Load image inside infoWindow
    fun loadImageMarker(url: String?, marker: Marker?,context: Context,mGoogleMap: GoogleMap?,isMarkerClick:Boolean? = false) {

        try {
            val target = imageTargetMarker(marker,context,mGoogleMap,isMarkerClick)

            Picasso.get()
                .load(url)
                .transform(ImageRoundCorners())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(target)

        }catch (e: IOException){
            e
        }


    }
}