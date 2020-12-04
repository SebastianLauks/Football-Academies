package lauks.sebastian.footballacademies.view.events

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_maps.*
import lauks.sebastian.footballacademies.BuildConfig
import lauks.sebastian.footballacademies.R
import java.lang.System.getProperty

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var autocompleteFragment: AutocompleteSupportFragment


    private var placeAddress: String? = null
    private var placeLatLng: LatLng? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

//        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        ActivityCompat.requestPermissions(this, permissions,0)

        initPlaces()
        initFab()



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private fun initFab(){
        fab.setOnClickListener {
            if (placeAddress != null && placeLatLng != null){
                val intent = Intent()
                intent.putExtra("placeAddress", placeAddress)
                intent.putExtra("placeLat", placeLatLng!!.latitude)
                intent.putExtra("placeLng", placeLatLng!!.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this, "Proszę wpisać miejscowość", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initPlaces(){
        // Initialize the SDK
        Places.initialize(applicationContext, BuildConfig.PLACES_API_KEY)
//        Places.initialize(applicationContext, resources.getString(R.string.api_key))

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.ADDRESS))




        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(status: Status) {
                Log.i("TAG", "An error occurred: $status")
            }

            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: ${place.latLng} ${place.address}")
                placeAddress = place.address
                placeLatLng = place.latLng
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(placeLatLng!!).title(place.name))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 16f))
            }


        })
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        setUpMap()






        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
                location ->
            Log.d("loca", location.toString())
            if(location != null){
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))


                val place = intent.getStringExtra("place")
                val lat = intent.getDoubleExtra("lat", 0.0)
                val lon = intent.getDoubleExtra("lon", 0.0)
                if (place != null && lat != 0.0 && lon != 0.0 ){
                    placeAddress = place
                    placeLatLng = LatLng(lat, lon)
                    autocompleteFragment.setText(place)
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(LatLng(lat, lon)).title(place))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 16f))
                }
            }else{

            }
        }

//         Add a marker in Sydney and move the camera
//        val sydney = LatLng(51.107883, 17.038538)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
