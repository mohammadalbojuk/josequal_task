package com.example.josequaltask.ui.fragments.home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import com.example.josequaltask.R
import com.example.josequaltask.adapter.landMarkAdapter.LandMarkAdapter
import com.example.josequaltask.databinding.FragmentMapsBinding
import com.example.josequaltask.model.landMark.LandMarkModel
import com.example.josequaltask.ui.fragments.dialog.DialogFragment
import com.example.josequaltask.utils.CustomSnapHelper
import com.example.josequaltask.utils.ImageLoad.loadImageMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.kml.KmlLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsFragment : Fragment(), LandMarkAdapter.LandMarkCallBackInterface {

    private var mGoogleMap: GoogleMap? = null

    private val landMarkMarkersList = mutableListOf<Marker?>()

    private val landMarkList = mutableListOf<LandMarkModel>()

    private val mSnapHelper = CustomSnapHelper()

    //binding
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    //view Model
    private val viewModel: MapsViewModel by viewModels()

    //adapter
    private val landMarkAdapter: LandMarkAdapter by lazy { LandMarkAdapter() }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        mGoogleMap = googleMap
        val amman = LatLng(31.970545, 35.913504)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amman, 15.2f))
        onMarkerClick(googleMap)

//        set custom style to the map by json file
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.style_json
            )
        )


    }




    @SuppressLint("PotentialBehaviorOverride")
    private fun onMarkerClick(googleMap: GoogleMap) {

        googleMap.setOnMarkerClickListener { marker ->
            val index = landMarkMarkersList.indexOfFirst { it?.title == marker.title }

            if (index != -1) {

//                Scroll Recycler view to the position of the the point
                binding.mapLandMarkRecyclerView.smoothScrollToPosition(index)

//                load the image inside infoWindow
                loadImageMarker(viewModel.landMarkList.value?.get(index)?.image, marker,requireContext(),mGoogleMap)

//                move camera on map to the point clicked
                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(marker.position), 350, null)
                lifecycleScope.launch {
                    delay(350)
                    loadImageMarker(viewModel.landMarkList.value?.get(index)?.image, marker,requireContext(),mGoogleMap)
                }

            } else {

                mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLng(marker.position), 250, null)
            }

            true
        }

        googleMap.setOnInfoWindowClickListener {marker ->
//            when click on infoWindow open dialog and send the details by newInstance
            val item = landMarkList.find { it.title == marker.title }
            val dialog =  DialogFragment.newInstance(listOf<LandMarkModel>(LandMarkModel(item?.image,item?.latitude.toString(),item?.longtitude.toString(),item?.title)))
            dialog.show(childFragmentManager, com.example.josequaltask.ui.fragments.dialog.DialogFragment.TAG)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        enterTransition = Fade(Fade.IN)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setupLandMarkRecyclerView()
        setUpObservers()

    }

    private fun setupLandMarkRecyclerView() {

        //layout manager
        binding.mapLandMarkRecyclerView.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        //snap helper
        mSnapHelper.attachToRecyclerView(binding.mapLandMarkRecyclerView)

        //adapter interface
        landMarkAdapter.setAdapterInterface(this)

        //adapter
        binding.mapLandMarkRecyclerView.adapter = landMarkAdapter


    }





    private fun setUpObservers() {

//        get KmlLayer from file
        val kmlInputStream = resources.openRawResource(R.raw.untitled_layer) // Replace with your KML file resource
        val kmlLayer = KmlLayer(mGoogleMap, kmlInputStream, requireContext())

        viewModel.setPoints(kmlLayer)

//        add markers and recycler view list after observe the points
        viewModel.landMarkList.observe(viewLifecycleOwner) {

            if (it != null) {
                val list = it

                val marker = BitmapFactory.decodeResource(resources, R.drawable.ic_pin_user)

                for (x in landMarkMarkersList) {
                    x?.remove()
                }
                landMarkMarkersList.clear()
                landMarkList.clear()
                if (list != null) {
                    list.forEach { landMarkList.add(it) }
                    for (i in list) {
                        val temp = mGoogleMap?.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    i.latitude?.toDouble() ?: 0.0,
                                    i.longtitude?.toDouble() ?: 0.0
                                )
                            ).title(i.title).draggable(false)
                                .icon(BitmapDescriptorFactory.fromBitmap(marker))
                        )
                        landMarkMarkersList.add(temp)
                    }

                    landMarkAdapter.submitList(list)
                }





            }
        }


    }

/*    when click on item in recycler view move camera
    to this item on map and show infoWindow
*/
    override fun onLandMarkClick(item: LandMarkModel) {

        val landMarkMarker = landMarkMarkersList.find { it?.title == item.title }

    if (!landMarkMarker?.isInfoWindowShown!!){
        loadImageMarker(item.image, landMarkMarker,requireContext(),mGoogleMap)

        mGoogleMap?.animateCamera(
            CameraUpdateFactory.newLatLng(landMarkMarker?.position!!),
            350,
            null
        )

        lifecycleScope.launch {
            delay(350)
            loadImageMarker(item.image, landMarkMarker,requireContext(),mGoogleMap)
        }
    }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}