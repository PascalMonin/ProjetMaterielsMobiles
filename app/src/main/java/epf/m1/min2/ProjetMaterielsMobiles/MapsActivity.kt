package epf.m1.min2.ProjetMaterielsMobiles

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.google.android.gms.cast.Cast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import epf.m1.min2.ProjetMaterielsMobiles.api.ApiInterface
import epf.m1.min2.ProjetMaterielsMobiles.api.RetroModel
import epf.m1.min2.ProjetMaterielsMobiles.dao.StationDao
import epf.m1.min2.ProjetMaterielsMobiles.databinding.ActivityMapsBinding
import epf.m1.min2.ProjetMaterielsMobiles.db.AppDatabase
import epf.m1.min2.ProjetMaterielsMobiles.entity.Station
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val Paris = LatLng(48.85, 2.34)
        //mMap.addMarker(MarkerOptions().position(Paris).title("Marker sur Paris"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 11.5f))
        mMap.clear()

        getResponse()

        createMarkers()
    }

    private fun getResponse() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiInterface.JSONURL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiInterface::class.java)
        val call = api.string
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.i("Responsestring", response.body().toString())
                //Toast.makeText()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString())
                        val jsonResponse = response.body().toString()
                        val jsonArray = JSONObject(jsonResponse)
                            .getJSONObject("data")
                            .getJSONArray("stations")

                        val stationList: ArrayList<Station> = ArrayList()
                        for (i in 0 until jsonArray.length()) {
                            val station : Station
                            val dataObj = jsonArray.getJSONObject(i)
                            station = Station(dataObj.getString("lat").toDouble(),dataObj.getString("lon").toDouble())
                            stationList.add(station)
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
    }

    private fun getDatabase(context: Context): AppDatabase {
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).allowMainThreadQueries().build()
        return db
    }

    private fun createMarkers(){
    try{
        val db = getDatabase(applicationContext)
        val listStation = ArrayList(db.stationDao().getAll())
        for (j in 0 until listStation.size) {
            mMap.addMarker(MarkerOptions()
                .position(LatLng(listStation[j].lat,listStation[j].lon))
                .title(listStation[j].lat.toString() + "/" + listStation[j].lon.toString()))
        }

    } catch (e: JSONException) {
        e.printStackTrace()
    }
}
    }

