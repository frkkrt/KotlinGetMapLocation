package com.fkurt.kotlingetmaplocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.fkurt.kotlingetmaplocation.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {
    private lateinit var design:ActivityMainBinding
    private  var permissionControl=0
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design= ActivityMainBinding.inflate(layoutInflater)
        setContentView(design.root)

        flpc= LocationServices.getFusedLocationProviderClient(this)


        design.buttonGetGps.setOnClickListener {
            permissionControl= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

            if(permissionControl>= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            }
            else
            {
                locationTask=flpc.lastLocation
                getGpsInfo()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getGpsInfo()
    {
        locationTask.addOnSuccessListener {
            if(it!=null)
            {
                design.textviewLatitude.text="Enlem: ${it.latitude}"
                design.textViewLongitude.text="Boylam: ${it.longitude}"
            }
            else
            {
                design.textviewLatitude.text="Enlem:Alınamadı"
                design.textViewLongitude.text="Boylam:Alınamadı"
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==100) {

            permissionControl= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "İzin kabul edildi", Toast.LENGTH_LONG).show()
                locationTask=flpc.lastLocation
                getGpsInfo()
            } else {
                Toast.makeText(applicationContext, "İzin reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}