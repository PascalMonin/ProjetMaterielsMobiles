package epf.m1.min2.ProjetMaterielsMobiles

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import epf.m1.min2.ProjetMaterielsMobiles.api.ApiInterface
import epf.m1.min2.ProjetMaterielsMobiles.api.ApiInterface2
import epf.m1.min2.ProjetMaterielsMobiles.api.RetroModel
import epf.m1.min2.ProjetMaterielsMobiles.databinding.ActivityMapsBinding
import epf.m1.min2.ProjetMaterielsMobiles.db.AppDatabase
import epf.m1.min2.ProjetMaterielsMobiles.entity.Station
import epf.m1.min2.ProjetMaterielsMobiles.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    val retroModelArrayList: ArrayList<RetroModel> = ArrayList()

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,HomeFragment(this))
        transaction.addToBackStack(null)
        transaction.commit()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var pos = LatLng(48.85, 2.34)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                    location ->
                if (location != null) {
                    pos = LatLng(location.latitude, location.latitude)
                }
            }

            return
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
        mMap.clear()

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this)

        getResponse()
        createMarkers()
    }

    private fun getResponse() {
        val retrofitInfo = Retrofit.Builder()
            .baseUrl(ApiInterface.JSONURL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val apiInfo = retrofitInfo.create(ApiInterface::class.java)
        val callInfo = apiInfo.string
        callInfo.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, responseInfo: Response<String?>) {
                if (responseInfo.isSuccessful) {
                    if (responseInfo.body() != null) {
                        val jsonResponse = responseInfo.body().toString()
                        val jsonArray = JSONObject(responseInfo.body().toString())
                            .getJSONObject("data")
                            .getJSONArray("stations")

                        val stationList: ArrayList<Station> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val station : Station
                            val dataObj = jsonArray.getJSONObject(i)
                            station = Station(
                                dataObj.getString("lat").toDouble(),
                                dataObj.getString("lon").toDouble(),
                                dataObj.getLong("station_id"),
                                dataObj.getString("name"),
                                dataObj.getInt("capacity"),
                                false,
                                false,
                                false,
                                "",
                                0,
                                0,
                                0,
                                "",
                                false
                            )
                            stationList.add(station)
                        }
                        val retrofitStatus = Retrofit.Builder()
                            .baseUrl(ApiInterface2.JSONURL2)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build()
                        val apiStatus = retrofitStatus.create(ApiInterface2::class.java)
                        val callStatus = apiStatus.string2
                        callStatus.enqueue(object : Callback<String?> {
                            override fun onResponse(call: Call<String?>, responseStatus: Response<String?>) {
                                if (responseStatus.isSuccessful) {
                                    if (responseStatus.body() != null) {
                                        val jsonResponse = responseStatus.body().toString()

                                        val obj2 = JSONObject(jsonResponse)
                                        val dataArray = obj2.getJSONObject("data")

                                        val stationsArray = dataArray.getJSONArray("stations")

                                        for(k in 0 until stationList.size){
                                            for(l in 0 until stationsArray.length()){
                                                val dataObj = stationsArray.getJSONObject(l)
                                                if (stationList[k].station_id==dataObj.getString("station_id").toLong()){
                                                    stationList[k].is_installed=
                                                        dataObj.getString("is_installed").toInt() == 1
                                                    stationList[k].is_renting=
                                                        dataObj.getString("is_renting").toInt() == 1
                                                    stationList[k].is_returning=
                                                        dataObj.getString("is_returning").toInt() == 1
                                                    stationList[k].numBikesAvailable=dataObj.getString("numBikesAvailable").toInt()
                                                }
                                            }
                                        }
                                        val list: List<Station> = stationList.toList()

                                        val db = getDatabase(applicationContext)
                                        db.stationDao().insertAll(list)
                                    } else {
                                        Log.i(
                                            "onEmptyResponse",
                                            "Returned empty response"
                                        ) //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }


                            override fun onFailure(call: Call<String?>, t: Throwable) {}
                        })
                    } else {
                        Log.i(
                            "onEmptyResponse",
                            "Returned empty response"
                        ) //Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {}
        })
    }

    private fun getDatabase(context: Context): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "stationDb"
        ).allowMainThreadQueries().build()
        return db
    }

    private fun createMarkers(){
        try{
            val db = getDatabase(applicationContext)
            val listStation = ArrayList(db.stationDao().getAll())
            for (j in 0 until listStation.size) {
                var marker = mMap.addMarker(MarkerOptions()
                    .position(LatLng(listStation[j].lat,listStation[j].lon))
                    .title(listStation[j].name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                marker?.tag=j
            }


        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {
        val db = getDatabase(applicationContext)

        val stationsList = ArrayList(db.stationDao().getAll())



        var position = marker.tag

        texte_info.text="La station peut louer des vélos : "+
                stationsList[position as Int].is_renting.toString()+"\n"+
                "Nombre de vélos disponibles : "+
                stationsList[position as Int].numBikesAvailable.toString()

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }


}