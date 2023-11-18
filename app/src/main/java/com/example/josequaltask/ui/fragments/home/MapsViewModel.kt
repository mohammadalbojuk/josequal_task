package com.example.josequaltask.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.josequaltask.model.landMark.LandMarkModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.data.kml.KmlLayer
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


class MapsViewModel :ViewModel() {

    private val _landMarkList = MutableLiveData<List<LandMarkModel>>()
    val landMarkList:LiveData<List<LandMarkModel>>
        get() = _landMarkList


//    get points from KmlLayer & added to list
    fun setPoints(kmlLayer: KmlLayer){

        val listOfPoints = arrayListOf<LandMarkModel>()
        try {
            val placemarks = kmlLayer.placemarks
            for (placemark in placemarks) {

                val name = placemark.getProperty("name")
                val description = placemark.getProperty("description")

                val point = placemark.geometry?.geometryObject as? LatLng
                point?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    listOfPoints.add(LandMarkModel(extractSrcFromCDATA(description),latitude.toString(),longitude.toString(),name))
                    listOfPoints.sortWith(compareBy { name ->
                        name.title
                    })
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }
        _landMarkList.postValue(listOfPoints)
    }

//    get Image Url from KML file
    private fun extractSrcFromCDATA(cdata: String): String? {
        val regex = """<img src="([^"]+)"[^>]*>""".toRegex()
        val matchResult = regex.find(cdata)
        return matchResult?.groupValues?.get(1)
    }

}