package com.example.josequaltask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.josequaltask.ui.fragments.home.MapsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = MapsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // R.id.fragment_container is the ID of the container in your activity's layout
            .addToBackStack(null) // Add transaction to back stack if needed
            .commit()

    }
}