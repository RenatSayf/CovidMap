package com.test.covidmap

import android.location.Address
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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
            getAddressFromLocation(latitude, longitude, 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    val country = if (list.isNotEmpty()) list[0].countryName else ""
                    val countryCode = if (list.isNotEmpty()) list[0].countryCode else ""
                    val dailyData = mapsViewModel.getDailyData(countryCode)

                    val clickLocation = LatLng(latitude, longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(clickLocation))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(clickLocation))

                    mMap.addMarker(MarkerOptions().position(clickLocation).title(country.plus(" $countryCode"))).showInfoWindow()

                    BottomDialog().show(supportFragmentManager, BottomDialog.TAG)
                }, { err ->
                    err.printStackTrace()
                })
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double, maxResult: Int) : Observable<List<Address>>
    {
        return Observable.create { emitter ->
            val listAddress: List<Address>
            try
            {
                listAddress = geocoder.getFromLocation(latitude, longitude, maxResult)
                emitter.onNext(listAddress)
            } catch (e: Exception)
            {
                emitter.onError(e)
            }
            finally
            {
                emitter.onComplete()
            }
        }
    }
}