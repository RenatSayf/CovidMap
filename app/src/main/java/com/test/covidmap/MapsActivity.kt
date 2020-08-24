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
import com.test.dialogs.BottomDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
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

        val moskva = LatLng(55.79490924736694, 37.61202726513147)
        mMap.addMarker(MarkerOptions().position(moskva).title("Moskva")).showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moskva))

        mMap.setOnMapClickListener {
            mMap.clear()
            val latitude = it.latitude
            val longitude = it.longitude
            val address = runBlocking {
                val async = async {
                    geocoder.getFromLocation(latitude, longitude, 1)
                }
                async.await()
            }
            //val address = geocoder.getFromLocation(latitude, longitude, 1)
            val country = if (address.size > 0) address[0].countryName else ""
            val countryCode = if (address.size > 0) address[0].countryCode else ""

            val dailyData = mapsViewModel.getDailyData(countryCode)

            val clickLocation = LatLng(latitude, longitude)
            mMap.animateCamera(CameraUpdateFactory.newLatLng(clickLocation))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(clickLocation))

            mMap.addMarker(MarkerOptions().position(clickLocation).title(country.plus(" $countryCode"))).showInfoWindow()

            BottomDialog().show(supportFragmentManager, BottomDialog.TAG)

            return@setOnMapClickListener
        }
    }
}