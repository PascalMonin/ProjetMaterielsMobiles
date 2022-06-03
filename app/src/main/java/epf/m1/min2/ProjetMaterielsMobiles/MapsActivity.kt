package epf.m1.min2.ProjetMaterielsMobiles

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import epf.m1.min2.ProjetMaterielsMobiles.api.ApiInterface
import epf.m1.min2.ProjetMaterielsMobiles.api.RetroModel
import epf.m1.min2.ProjetMaterielsMobiles.databinding.ActivityMapsBinding
import kotlinx.android.synthetic.main.activity_maps.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val retroModelArrayList: ArrayList<RetroModel> = ArrayList()

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

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this)

        getResponse()
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
                        createMarkers(jsonResponse)
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

    private fun createMarkers(response: String) {
        try {
            //getting the whole json object from the response
            val obj = JSONObject(response)


            Toast.makeText(this@MapsActivity, "test", Toast.LENGTH_SHORT)
                .show()
            val dataArray = obj.getJSONObject("data") //.getJSONArray("data")

            val stationsArray = dataArray.getJSONArray("stations")

            for (i in 0 until stationsArray.length()) {
                val retroModel = RetroModel()
                val dataObj = stationsArray.getJSONObject(i)
                retroModel.station_id=dataObj.getString("station_id").toLong()
                retroModel.name=dataObj.getString("name")
                retroModel.lat = dataObj.getString("lat").toDouble()
                retroModel.lon = dataObj.getString("lon").toDouble()
                retroModel.capacity=dataObj.getString("capacity").toInt()
                retroModel.stationCode=dataObj.getString("stationCode")
                retroModelArrayList.add(retroModel)

            }
            for (j in 0 until retroModelArrayList.size) {
                var marker = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(retroModelArrayList[j].lat, retroModelArrayList[j].lon))
                        .title("Station")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
                marker?.tag=j
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_velo))

            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {

        var position = marker.tag

        infos_station.text=""
        infos_station.text=retroModelArrayList[position as Int].station_id.toString()


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

}