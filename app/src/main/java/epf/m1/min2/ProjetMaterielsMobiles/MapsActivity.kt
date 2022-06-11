package epf.m1.min2.ProjetMaterielsMobiles

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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
import epf.m1.min2.ProjetMaterielsMobiles.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    val retroModelArrayList: ArrayList<RetroModel> = ArrayList()




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



    private fun createMarkers(response: String) = try {
        //getting the whole json object from the response
        val obj = JSONObject(response)

        Toast.makeText(this@MapsActivity, "test", Toast.LENGTH_SHORT)
            .show()
        var dataArray = obj.getJSONObject("data") //.getJSONArray("data")

        var stationsArray = dataArray.getJSONArray("stations")

        for (i in 0 until stationsArray.length()) {
            val dataObj = stationsArray.getJSONObject(i)
            val retroModel = RetroModel(

                    dataObj.getString("station_id").toLong(),
                    dataObj.getString("name"),
                    dataObj.getString("lat").toDouble(),
                    dataObj.getString("lon").toDouble(),
                    dataObj.getString("capacity").toInt(),
                    dataObj.getString("stationCode"),
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

        detailsStation()

    } catch (e: JSONException) {
        e.printStackTrace()
    }


    private fun detailsStation(){

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiInterface2.JSONURL2)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val api = retrofit.create(ApiInterface2::class.java)
        val call = api.string2
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>, response: Response<String?>) {
                Log.i("Responsestring", response.body().toString())
                //Toast.makeText()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString())
                        val jsonResponse = response.body().toString()


                        val obj2 = JSONObject(jsonResponse)
                        val dataArray = obj2.getJSONObject("data") //.getJSONArray("data")

                        val stationsArray = dataArray.getJSONArray("stations")


                        for(k in 0 until retroModelArrayList.size){
                            for(l in 0 until stationsArray.length()){
                                val dataObj = stationsArray.getJSONObject(l)
                                if (retroModelArrayList[k].station_id==dataObj.getString("station_id").toLong()){
                                    retroModelArrayList[k].is_installed=
                                        dataObj.getString("is_installed").toInt() == 1
                                    retroModelArrayList[k].is_renting=
                                        dataObj.getString("is_renting").toInt() == 1
                                    retroModelArrayList[k].is_returning=
                                        dataObj.getString("is_returning").toInt() == 1
                                    retroModelArrayList[k].numBikesAvailable=dataObj.getString("numBikesAvailable").toInt()
                                }
                            }
                        }



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



    /** Called when the user clicks a marker.  */
    override fun onMarkerClick(marker: Marker): Boolean {

        var position = marker.tag

        texte_info.text="La station peut louer des vélos : "+
                retroModelArrayList[position as Int].is_renting.toString()+"\n"+
                "Nombre de vélos disponibles : "+
                retroModelArrayList[position as Int].numBikesAvailable.toString()

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }


}