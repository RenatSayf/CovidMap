package com.test.covidmap

import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
{
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap)
    {
        geocoder = Geocoder(this, Locale.US)
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val moskva = LatLng(55.79490924736694, 37.61202726513147)
        mMap.addMarker(MarkerOptions().position(moskva).title("Marker in Moskva"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moskva))
        mMap.setOnMapClickListener {
            val latitude = it.latitude
            val longitude = it.longitude
            val address = geocoder.getFromLocation(latitude, longitude, 1)
            val country = address[0].countryName
            return@setOnMapClickListener
        }
    }
}