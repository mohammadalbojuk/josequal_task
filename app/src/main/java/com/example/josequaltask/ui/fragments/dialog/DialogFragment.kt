package com.example.josequaltask.ui.fragments.dialog

import android.R
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.josequaltask.databinding.FragmentDialogBinding
import com.example.josequaltask.model.landMark.LandMarkModel
import com.example.josequaltask.utils.CustomInfoWindowAdapter
import com.example.josequaltask.utils.ImageLoad.loadImage
import com.example.josequaltask.utils.ImageRoundCorners
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException


class DialogFragment : DialogFragment() {

    private var _binding: FragmentDialogBinding?=null
    private val binding get() = _binding!!

    private var selectedItem:ArrayList<LandMarkModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentDialogBinding.inflate(inflater,container,false)
        return binding.root    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setupUi()
    }

    private fun getArgs(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            selectedItem = arguments?.getParcelableArrayList(ARG_SELECTED_ITEMS,LandMarkModel::class.java)
        } else {
            try {
                selectedItem =
                    arguments?.getParcelableArrayList(ARG_SELECTED_ITEMS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun setupUi(){

        loadImage(selectedItem?.get(0)?.image, binding.dialogImage)
        binding.dialogTitel.text = selectedItem?.get(0)?.title
        binding.dialogDesc.text = selectedItem?.get(0)?.subtitle
    }





    companion object {
        const val TAG = "YourDialogFragmentTag"
        private const val  ARG_SELECTED_ITEMS = "SELECTEDITEMS"

        fun newInstance(selectedItemList: List<LandMarkModel>): com.example.josequaltask.ui.fragments.dialog.DialogFragment {
            val args = Bundle()
            args.putParcelableArrayList(ARG_SELECTED_ITEMS, ArrayList(selectedItemList))
            val fragment = com.example.josequaltask.ui.fragments.dialog.DialogFragment()
            fragment.arguments = args
            return fragment
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
