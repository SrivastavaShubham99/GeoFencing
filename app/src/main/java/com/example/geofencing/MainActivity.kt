package com.example.geofencing

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import com.example.geofencing.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.*


class MainActivity : AppCompatActivity(),GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
    lateinit var googleApiClient:GoogleApiClient
    lateinit var locationRequest:LocationRequest
    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        getPermissionsForLocation()
        initializing()
       getLocation()
    }

    private fun getPermissionsForLocation(){
        if (ContextCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) !==
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION) ===
                                    PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
private fun initializing(){
    googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    locationRequest = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY) //GPS quality location points
            .setInterval(2000) //At least once every 2 seconds
            .setFastestInterval(1000) //At most once a second
}

    private fun getLocation(){
        Log.d("location","isRunning at step1")
        var location:Location
        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d("location","isRunning at step2")

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("location","isRunning at step3")
        }

        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
        var address:Address
        var geocoder= Geocoder(this, Locale.getDefault())
        val addressess = geocoder.getFromLocation(location.latitude,location.longitude,1)
        address= addressess[0]
        Log.d("location","here is the value $address")
        mBinding.longitude.text=address.toString()
    }
    override fun onConnected(p0: Bundle?) {
        Toast.makeText(this,"Connection SuccessFull",Toast.LENGTH_LONG).show()
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this,"Connection UnSuccessFull",Toast.LENGTH_LONG).show()
    }

    override fun onLocationChanged(p0: Location) {
        var address:Address?=null
        var geocoder:Geocoder= Geocoder(this, Locale.getDefault())
        var addressess:List<Address>
        try {
            addressess=geocoder.getFromLocation(p0.longitude,p0.altitude,1)
            address=addressess.get(0)
            Log.d("addressLocation","address : $address")
            mBinding.longitude.text= address.toString()
        }
        catch (err:Error){
            Log.d("tag","here is the solution $err")
        }
    }
}