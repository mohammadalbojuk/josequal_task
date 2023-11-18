package com.example.josequaltask.model.landMark

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LandMarkModel(
    @SerializedName("image")
val image: String?,
    @SerializedName("latitude")
val latitude: String?,
    @SerializedName("longtitude")
val longtitude: String?,
    @SerializedName("title")
val title: String?,
    @SerializedName("distanceInMeters")
    val distanceInMeters: String? = "500",
    @SerializedName("subtitle")
    val subtitle: String? = "Amman, Jordan"): Parcelable {



}


